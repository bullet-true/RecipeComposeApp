package com.ifedorov.recipecomposeapp.core.network.api

import androidx.test.ext.junit.runners.AndroidJUnit4
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
class RecipesApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: RecipesApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(TEST_BASE_PATH))
            .addConverterFactory(
                json.asConverterFactory(APPLICATION_JSON_MEDIA_TYPE)
            )
            .build()
            .create(RecipesApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun categoriesJsonIsDeserializedCorrectly() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(createCategoriesJson(size = TEST_CATEGORIES_SIZE))
        )

        val result = apiService.getCategories()

        assertEquals(TEST_CATEGORIES_SIZE, result.size)
        assertEquals(TEST_FIRST_CATEGORY_ID, result.first().id)
        assertEquals(TEST_FIRST_CATEGORY_TITLE, result.first().title)
        assertEquals(TEST_FIRST_CATEGORY_DESCRIPTION, result.first().description)
        assertEquals(TEST_FIRST_CATEGORY_IMAGE_URL, result.first().imageUrl)

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals(EXPECTED_CATEGORIES_PATH, recordedRequest.path)
    }

    @Test
    fun ignoresUnknownFieldsInCategoriesJson() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HTTP_OK)
                .setBody(CATEGORY_WITH_UNKNOWN_FIELD_JSON)
        )

        val result = apiService.getCategories()

        assertEquals(1, result.size)
        assertEquals(TEST_FIRST_CATEGORY_TITLE, result.first().title)
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
        private const val TEST_FIRST_CATEGORY_ID = 0
        private const val TEST_FIRST_CATEGORY_TITLE = "title_0"
        private const val TEST_FIRST_CATEGORY_DESCRIPTION = "description_0"
        private const val TEST_FIRST_CATEGORY_IMAGE_URL = "imageUrl_0"
        private const val HTTP_OK = 200
        private const val TEST_BASE_PATH = "/"
        private const val EXPECTED_CATEGORIES_PATH = "/category"
        private const val JSON_ARRAY_START = "["
        private const val JSON_ARRAY_END = "]"
        private const val JSON_ITEM_SEPARATOR = ","
        private const val CATEGORY_WITH_UNKNOWN_FIELD_JSON = """
            [
              {
                "id": 0,
                "title": "title_0",
                "description": "description_0",
                "imageUrl": "imageUrl_0",
                "unknownField": "ignored"
              }
            ]
        """

        private val APPLICATION_JSON_MEDIA_TYPE = "application/json".toMediaType()
    }
}