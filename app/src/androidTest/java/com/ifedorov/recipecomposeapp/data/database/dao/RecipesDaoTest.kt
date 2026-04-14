package com.ifedorov.recipecomposeapp.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ifedorov.recipecomposeapp.data.database.RecipesDatabase
import com.ifedorov.recipecomposeapp.data.database.entity.CategoryEntity
import com.ifedorov.recipecomposeapp.data.database.entity.RecipeEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RecipesDaoTest {

    private lateinit var database: RecipesDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var recipeDao: RecipeDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(context, RecipesDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        categoryDao = database.categoryDao()
        recipeDao = database.recipeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertsAndRetrievesCategories() = runTest {
        val categories = createCategories(TEST_CATEGORIES_SIZE)
        categoryDao.insertCategories(categories)

        val retrieved = categoryDao.getCategories().first()

        assertEquals(TEST_CATEGORIES_SIZE, retrieved.size)
        assertEquals(TEST_FIRST_CATEGORY_ID, retrieved.first().id)
        assertEquals(TEST_SECOND_CATEGORY_ID, retrieved.last().id)
        assertEquals(TEST_FIRST_CATEGORY_NAME, retrieved.first().name)
        assertEquals(TEST_SECOND_CATEGORY_NAME, retrieved.last().name)

    }

    @Test
    fun insertReplacesDuplicateCategory() = runTest {
        val original = createCategories(size = 1).first()
        val updated = original.copy(
            name = UPDATED_CATEGORY_NAME,
            description = UPDATED_CATEGORY_DESCRIPTION,
            imageUrl = UPDATED_CATEGORY_IMAGE_URL
        )

        categoryDao.insertCategories(listOf(original))
        categoryDao.insertCategories(listOf(updated))

        val retrieved = categoryDao.getCategories().first()

        assertEquals(1, retrieved.size)
        assertEquals(UPDATED_CATEGORY_NAME, retrieved.first().name)
        assertEquals(UPDATED_CATEGORY_DESCRIPTION, retrieved.first().description)
        assertEquals(UPDATED_CATEGORY_IMAGE_URL, retrieved.first().imageUrl)
    }

    @Test
    fun getRecipesByCategoryReturnsCorrectItems() = runTest {
        val recipesForFirstCategory = createRecipes(
            size = TEST_RECIPES_FOR_FIRST_CATEGORY_SIZE,
            categoryId = TEST_FIRST_CATEGORY_ID,
            startId = TEST_FIRST_CATEGORY_START_RECIPE_ID
        )

        val recipesForSecondCategory = createRecipes(
            size = TEST_RECIPES_FOR_SECOND_CATEGORY_SIZE,
            categoryId = TEST_SECOND_CATEGORY_ID,
            startId = TEST_SECOND_CATEGORY_START_RECIPE_ID
        )

        recipeDao.insertRecipes(recipesForFirstCategory)
        recipeDao.insertRecipes(recipesForSecondCategory)

        val result = recipeDao.getRecipesByCategoryId(TEST_FIRST_CATEGORY_ID).first()

        assertEquals(TEST_RECIPES_FOR_FIRST_CATEGORY_SIZE, result.size)
        assertTrue(result.all { it.categoryId == TEST_FIRST_CATEGORY_ID })
        assertEquals(
            listOf(TEST_FIRST_RECIPE_ID, TEST_SECOND_RECIPE_ID),
            result.map { it.id }
        )
    }

    @Test
    fun emptyDatabaseReturnsEmptyList() = runTest {
        val result = categoryDao.getCategories().first()
        assertTrue(result.isEmpty())
    }

    private fun createCategories(size: Int): List<CategoryEntity> =
        List(size) { index ->
            CategoryEntity(
                id = index,
                name = "name_$index",
                description = "description_$index",
                imageUrl = "imageUrl_$index"
            )
        }

    private fun createRecipes(
        size: Int,
        categoryId: Int,
        startId: Int = 0
    ): List<RecipeEntity> =
        List(size) { index ->
            val recipeId = startId + index

            RecipeEntity(
                id = recipeId,
                title = "recipe_$recipeId",
                categoryId = categoryId,
                imageUrl = "image_$recipeId.jpg",
                ingredients = listOf("100###г###ingredient_$recipeId"),
                method = listOf("step_$recipeId")
            )
        }

    companion object {
        private const val TEST_CATEGORIES_SIZE = 2
        private const val TEST_FIRST_CATEGORY_ID = 0
        private const val TEST_SECOND_CATEGORY_ID = 1
        private const val TEST_FIRST_CATEGORY_NAME = "name_0"
        private const val TEST_SECOND_CATEGORY_NAME = "name_1"
        private const val UPDATED_CATEGORY_NAME = "updated_name"
        private const val UPDATED_CATEGORY_DESCRIPTION = "updated_description"
        private const val UPDATED_CATEGORY_IMAGE_URL = "updated_imageUrl"
        private const val TEST_RECIPES_FOR_FIRST_CATEGORY_SIZE = 2
        private const val TEST_RECIPES_FOR_SECOND_CATEGORY_SIZE = 1
        private const val TEST_FIRST_RECIPE_ID = 0
        private const val TEST_SECOND_RECIPE_ID = 1
        private const val TEST_FIRST_CATEGORY_START_RECIPE_ID = 0
        private const val TEST_SECOND_CATEGORY_START_RECIPE_ID = 3
    }
}