package com.ifedorov.recipecomposeapp.data.repository

import com.ifedorov.recipecomposeapp.data.model.CategoryDto
import com.ifedorov.recipecomposeapp.data.model.RecipeDto

interface RecipesRepository {
    suspend fun getCategories(): List<CategoryDto>
    suspend fun getRecipesByCategory(categoryId: Int): List<RecipeDto>
    suspend fun getRecipe(recipeId: Int): RecipeDto
}