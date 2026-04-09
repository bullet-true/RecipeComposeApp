package com.ifedorov.recipecomposeapp.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.ifedorov.recipecomposeapp.core.network.api.RecipesApiService
import com.ifedorov.recipecomposeapp.data.database.RecipesDatabase
import com.ifedorov.recipecomposeapp.data.database.dao.CategoryDao
import com.ifedorov.recipecomposeapp.data.database.entity.CategoryEntity
import com.ifedorov.recipecomposeapp.data.model.CategoryDto
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RecipesRepositoryIntegrationTest {

    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao

    private val apiService = mockk<RecipesApiService>()
    private lateinit var repository: RecipesRepositoryImpl

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        categoryDao = database.categoryDao()
        repository = RecipesRepositoryImpl(
            api = apiService,
            database = database,
            ioDispatcher = UnconfinedTestDispatcher()
        )
    }

    @After
    fun tearDown() {
        database.close()
        clearAllMocks()
    }

    @Test
    fun savesDataToCacheAfterSuccessfulApiCall() = runTest {
        val categoriesFromApi = createCategoryDtos(size = TEST_CATEGORIES_SIZE)

        coEvery { apiService.getCategories() } returns categoriesFromApi

        repository.getCategories().test {
            awaitItem()
            val loaded = awaitItem()

            assertEquals(TEST_CATEGORIES_SIZE, loaded.size)
            assertEquals(TEST_FIRST_CATEGORY_ID, loaded.first().id)
            assertEquals(TEST_FIRST_CATEGORY_TITLE, loaded.first().title)
            assertEquals(TEST_FIRST_CATEGORY_DESCRIPTION, loaded.first().description)
            assertEquals(TEST_FIRST_CATEGORY_IMAGE_URL, loaded.first().imageUrl)

            cancelAndIgnoreRemainingEvents()
        }

        val cached = categoryDao.getCategories().first()

        assertEquals(TEST_CATEGORIES_SIZE, cached.size)
        assertEquals(TEST_FIRST_CATEGORY_ID, cached.first().id)
        assertEquals(TEST_FIRST_CATEGORY_TITLE, cached.first().name)
        assertEquals(TEST_FIRST_CATEGORY_DESCRIPTION, cached.first().description)
        assertEquals(TEST_FIRST_CATEGORY_IMAGE_URL, cached.first().imageUrl)
    }

    @Test
    fun returnsCachedDataWhenApiFails() = runTest {
        val cachedCategories = createCategoryEntities(size = TEST_CATEGORIES_SIZE)
        categoryDao.insertCategories(cachedCategories)

        coEvery { apiService.getCategories() } throws IOException(API_ERROR_MESSAGE)

        repository.getCategories().test {
            val loaded = awaitItem()

            assertEquals(TEST_CATEGORIES_SIZE, loaded.size)
            assertEquals(TEST_FIRST_CATEGORY_ID, loaded.first().id)
            assertEquals(TEST_FIRST_CATEGORY_TITLE, loaded.first().title)
            assertEquals(TEST_FIRST_CATEGORY_DESCRIPTION, loaded.first().description)
            assertEquals(TEST_FIRST_CATEGORY_IMAGE_URL, loaded.first().imageUrl)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createCategoryDtos(size: Int): List<CategoryDto> =
        List(size) { index ->
            CategoryDto(
                id = index,
                title = "title_$index",
                description = "description_$index",
                imageUrl = "imageUrl_$index"
            )
        }

    private fun createCategoryEntities(size: Int): List<CategoryEntity> =
        List(size) { index ->
            CategoryEntity(
                id = index,
                name = "title_$index",
                description = "description_$index",
                imageUrl = "imageUrl_$index"
            )
        }

    companion object {
        private const val TEST_CATEGORIES_SIZE = 2
        private const val TEST_FIRST_CATEGORY_ID = 0
        private const val TEST_FIRST_CATEGORY_TITLE = "title_0"
        private const val TEST_FIRST_CATEGORY_DESCRIPTION = "description_0"
        private const val TEST_FIRST_CATEGORY_IMAGE_URL = "imageUrl_0"
        private const val API_ERROR_MESSAGE = "Network error"
    }
}