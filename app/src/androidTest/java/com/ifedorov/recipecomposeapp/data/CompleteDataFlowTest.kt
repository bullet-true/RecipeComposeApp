package com.ifedorov.recipecomposeapp.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.ifedorov.recipecomposeapp.core.network.api.RecipesApiService
import com.ifedorov.recipecomposeapp.data.database.RecipesDatabase
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@RunWith(AndroidJUnit4::class)
class CompleteDataFlowTest {
    private lateinit var database: RecipesDatabase
    private lateinit var mockWebServer: MockWebServer
    private lateinit var repository: RecipesRepositoryImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        mockWebServer = MockWebServer()
        mockWebServer.start()

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        val apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(TEST_BASE_PATH))
            .addConverterFactory(
                json.asConverterFactory(APPLICATION_JSON_MEDIA_TYPE)
            )
            .build()
            .create(RecipesApiService::class.java)

        repository = RecipesRepositoryImpl(
            api = apiService,
            database = database,
            ioDispatcher = UnconfinedTestDispatcher()
        )
    }

    @After
    fun tearDown() {
        database.close()
        mockWebServer.shutdown()
    }

    @Test
    fun categoriesAreLoadedFromApiAndStoredInCache() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(createCategoriesJson(size = TEST_CATEGORIES_SIZE))
        )

        repository.getCategories().test {
            val loaded = awaitItem()

            assertEquals(TEST_CATEGORIES_SIZE, loaded.size)

            assertEquals(TEST_CATEGORY_ID, loaded.first().id)
            assertEquals(TEST_CATEGORY_TITLE, loaded.first().title)
            assertEquals(TEST_CATEGORY_DESCRIPTION, loaded.first().description)
            assertEquals(TEST_CATEGORY_IMAGE_URL, loaded.first().imageUrl)

            cancelAndIgnoreRemainingEvents()
        }

        val cached = database.categoryDao().getCategories().first()

        assertEquals(TEST_CATEGORIES_SIZE, cached.size)
        assertEquals(TEST_CATEGORY_ID, cached.first().id)
        assertEquals(TEST_CATEGORY_TITLE, cached.first().name)
        assertEquals(TEST_CATEGORY_DESCRIPTION, cached.first().description)
        assertEquals(TEST_CATEGORY_IMAGE_URL, cached.first().imageUrl)
    }

    private fun createCategoriesJson(size: Int): String =
        buildString {
            append(JSON_ARRAY_START)
            repeat(size) { index ->
                append(
                    createCategoryJson(
                        id = index,
                        title = "title_$index",
                        description = "description_$index",
                        imageUrl = "imageUrl_$index"
                    )
                )
                if (index != size - 1) {
                    append(JSON_ITEM_SEPARATOR)
                }
            }
            append(JSON_ARRAY_END)
        }

    private fun createCategoryJson(
        id: Int,
        title: String,
        description: String,
        imageUrl: String
    ): String =
        """
        {
          "id": $id,
          "title": "$title",
          "description": "$description",
          "imageUrl": "$imageUrl"
        }
        """.trimIndent()

    companion object {
        private const val TEST_CATEGORIES_SIZE = 2
        private const val TEST_CATEGORY_ID = 0
        private const val TEST_CATEGORY_TITLE = "title_0"
        private const val TEST_CATEGORY_DESCRIPTION = "description_0"
        private const val TEST_CATEGORY_IMAGE_URL = "imageUrl_0"
        private const val HTTP_OK = 200
        private const val TEST_BASE_PATH = "/"
        private const val JSON_ARRAY_START = "["
        private const val JSON_ARRAY_END = "]"
        private const val JSON_ITEM_SEPARATOR = ","
        private val APPLICATION_JSON_MEDIA_TYPE = "application/json".toMediaType()
    }
}