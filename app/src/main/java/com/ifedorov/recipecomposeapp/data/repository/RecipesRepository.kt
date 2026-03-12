package com.ifedorov.recipecomposeapp.data.repository

import com.ifedorov.recipecomposeapp.data.model.CategoryDto
import com.ifedorov.recipecomposeapp.data.model.RecipeDto
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun getCategories(): Flow<List<CategoryDto>>
    fun getRecipesByCategory(categoryId: Int): Flow<List<RecipeDto>>
    fun getRecipe(recipeId: Int): Flow<RecipeDto?>
    fun getRecipesByIds(recipeIds: List<Int>): Flow<List<RecipeDto>>
}