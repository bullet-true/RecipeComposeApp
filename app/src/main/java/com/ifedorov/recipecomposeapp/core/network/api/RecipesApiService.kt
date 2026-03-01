package com.ifedorov.recipecomposeapp.core.network.api

import com.ifedorov.recipecomposeapp.data.model.CategoryDto
import com.ifedorov.recipecomposeapp.data.model.RecipeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipesApiService {
    @GET("category")
    suspend fun getCategories(): List<CategoryDto>

    @GET("category/{id}/recipes")
    suspend fun getRecipesByCategoryId(@Path("id") categoryId: Int): List<RecipeDto>
}