package com.ifedorov.recipecomposeapp.data.repository

import app.cash.turbine.test
import com.ifedorov.recipecomposeapp.core.network.api.RecipesApiService
import com.ifedorov.recipecomposeapp.data.database.RecipesDatabase
import com.ifedorov.recipecomposeapp.data.database.dao.CategoryDao
import com.ifedorov.recipecomposeapp.data.database.dao.RecipeDao
import com.ifedorov.recipecomposeapp.data.database.entity.CategoryEntity
import com.ifedorov.recipecomposeapp.data.database.entity.RecipeEntity
import fixtures.CategoryTestFixtures.createCategoryDtoList
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.After
import org.junit.Before
import org.junit.Test

class RecipesRepositoryTest {

    private val apiService = mockk<RecipesApiService>()
    private val database = mockk<RecipesDatabase>(relaxed = true)
    private val categoryDao = mockk<CategoryDao>()
    private val recipeDao = mockk<RecipeDao>()

    private lateinit var repository: RecipesRepositoryImpl

    @Before
    fun setup() {
        every { database.categoryDao() } returns categoryDao
        every { database.recipeDao() } returns recipeDao
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getCategories emits categories from database`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)

        repository = RecipesRepositoryImpl(
            api = apiService,
            database = database,
            ioDispatcher = testDispatcher
        )

        every { categoryDao.getCategories() } returns flowOf(CATEGORY_ENTITIES)
        coEvery { apiService.getCategories() } returns createCategoryDtoList(1)
        coEvery { categoryDao.insertCategories(any()) } just Runs

        repository.getCategories().test {
            advanceUntilIdle()

            val result = awaitItem()

            assertEquals(1, result.size)
            assertEquals(CATEGORY_ID, result.first().id)
            assertEquals(CATEGORY_NAME, result.first().title)
            assertEquals(CATEGORY_DESCRIPTION, result.first().description)
            assertEquals(CATEGORY_IMAGE_URL, result.first().imageUrl)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify { categoryDao.insertCategories(any()) }
    }

    @Test
    fun `getCategories still emits data when api throws exception`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)

        repository = RecipesRepositoryImpl(
            api = apiService,
            database = database,
            ioDispatcher = testDispatcher
        )

        every { categoryDao.getCategories() } returns flowOf(CATEGORY_ENTITIES)
        coEvery { apiService.getCategories() } throws IOException(API_ERROR_MESSAGE)

        repository.getCategories().test {
            advanceUntilIdle()

            val result = awaitItem()

            assertEquals(1, result.size)
            assertEquals(CATEGORY_ID, result.first().id)
            assertEquals(CATEGORY_NAME, result.first().title)
            assertEquals(CATEGORY_DESCRIPTION, result.first().description)
            assertEquals(CATEGORY_IMAGE_URL, result.first().imageUrl)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 0) { categoryDao.insertCategories(any()) }
    }

    @Test
    fun `getRecipesByCategory returns flow filtered by categoryId`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)

        repository = RecipesRepositoryImpl(
            api = apiService,
            database = database,
            ioDispatcher = testDispatcher
        )

        every { recipeDao.getRecipesByCategoryId(CATEGORY_ID) } returns flowOf(RECIPE_ENTITIES)
        coEvery { apiService.getRecipesByCategoryId(CATEGORY_ID) } returns emptyList()
        coEvery { recipeDao.insertRecipes(any()) } just Runs

        repository.getRecipesByCategory(CATEGORY_ID).test {
            advanceUntilIdle()

            val result = awaitItem()

            assertEquals(2, result.size)
            assertEquals(listOf(RECIPE_ID_1, RECIPE_ID_2), result.map { it.id })
            assertEquals(listOf(RECIPE_TITLE_1, RECIPE_TITLE_2), result.map { it.title })

            cancelAndIgnoreRemainingEvents()
        }

        coVerify { recipeDao.insertRecipes(any()) }
    }

    companion object {
        private const val CATEGORY_ID = 0
        private const val CATEGORY_NAME = "Бургеры"
        private const val CATEGORY_DESCRIPTION = "Рецепты всех популярных видов бургеров"
        private const val CATEGORY_IMAGE_URL = "burgers.png"

        private const val RECIPE_ID_1 = 2
        private const val RECIPE_ID_2 = 3
        private const val RECIPE_TITLE_1 = "Бургер"
        private const val RECIPE_TITLE_2 = "Салат"
        private const val RECIPE_IMAGE_URL_1 = "burger.jpg"
        private const val RECIPE_IMAGE_URL_2 = "salad.jpg"
        private const val INGREDIENT_1 = "100###г###Фарш"
        private const val INGREDIENT_2 = "100###г###Листья салата"

        private const val API_ERROR_MESSAGE = "Network error"
        private val CATEGORY_ENTITIES = listOf(
            CategoryEntity(
                id = CATEGORY_ID,
                name = CATEGORY_NAME,
                description = CATEGORY_DESCRIPTION,
                imageUrl = CATEGORY_IMAGE_URL
            )
        )

        private val RECIPE_ENTITIES = listOf(
            RecipeEntity(
                id = RECIPE_ID_1,
                title = RECIPE_TITLE_1,
                categoryId = CATEGORY_ID,
                imageUrl = RECIPE_IMAGE_URL_1,
                ingredients = listOf(INGREDIENT_1),
                method = listOf("Пункт 1")
            ),
            RecipeEntity(
                id = RECIPE_ID_2,
                title = RECIPE_TITLE_2,
                categoryId = CATEGORY_ID,
                imageUrl = RECIPE_IMAGE_URL_2,
                ingredients = listOf(INGREDIENT_2),
                method = listOf("Пункт 2")
            )
        )
    }
}