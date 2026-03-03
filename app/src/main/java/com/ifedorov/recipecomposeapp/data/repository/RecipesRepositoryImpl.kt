package com.ifedorov.recipecomposeapp.data.repository

import android.util.Log
import com.ifedorov.recipecomposeapp.core.network.api.RecipesApiService
import com.ifedorov.recipecomposeapp.data.model.CategoryDto
import com.ifedorov.recipecomposeapp.data.model.RecipeDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipesRepositoryImpl(private val api: RecipesApiService) : RecipesRepository {

    override suspend fun getCategories(): List<CategoryDto> =
        withContext(Dispatchers.IO) {
            try {
                api.getCategories()
            } catch (e: Exception) {
                Log.e("Repository", "Ошибка при получении категорий", e)
                emptyList()
            }
        }

    override suspend fun getRecipesByCategory(categoryId: Int): List<RecipeDto> =
        withContext(Dispatchers.IO) {
            try {
                api.getRecipesByCategoryId(categoryId)
            } catch (e: Exception) {
                Log.e("Repository", "Ошибка при получении рецептов категории", e)
                emptyList()
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