package com.ifedorov.recipecomposeapp.data.repository

import android.util.Log
import com.ifedorov.recipecomposeapp.core.network.api.RecipesApiService
import com.ifedorov.recipecomposeapp.data.database.RecipesDatabase
import com.ifedorov.recipecomposeapp.data.model.CategoryDto
import com.ifedorov.recipecomposeapp.data.model.RecipeDto
import com.ifedorov.recipecomposeapp.data.model.toDto
import com.ifedorov.recipecomposeapp.data.model.toEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipesRepositoryImpl(
    private val api: RecipesApiService,
    database: RecipesDatabase,
) : RecipesRepository {

    private val categoryDao = database.categoryDao()
    private val recipeDao = database.recipeDao()

    override fun getCategories(): Flow<List<CategoryDto>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val categoriesFromApi = api.getCategories()
                val categoriesToEntity = categoriesFromApi.map { it.toEntity() }
                categoryDao.insertCategories(categoriesToEntity)
            } catch (e: Exception) {
                Log.e("Repository", "Ошибка при получении категорий из API", e)
            }
        }

        return categoryDao.getCategories().map { categories ->
            categories.map { it.toDto() }
        }
    }


    override fun getRecipesByCategory(categoryId: Int): Flow<List<RecipeDto>> {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val recipesFromApi = api.getRecipesByCategoryId(categoryId)
                val recipesToEntity = recipesFromApi.map { it.toEntity(categoryId) }
                recipeDao.insertRecipes(recipesToEntity)
            } catch (e: Exception) {
                Log.e("Repository", "Ошибка при получении рецептов категории из API", e)
            }
        }

        return recipeDao.getRecipesByCategoryId(categoryId).map { recipes ->
            recipes.map { it.toDto() }
        }
    }

    override suspend fun getRecipe(recipeId: Int): RecipeDto =
        withContext(Dispatchers.IO) {
            try {
                api.getRecipe(recipeId)
            } catch (e: Exception) {
                Log.e("Repository", "Ошибка при получении рецепта", e)
                throw e
            }
        }
}