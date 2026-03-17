package com.ifedorov.recipecomposeapp.data.repository

import android.util.Log
import com.ifedorov.recipecomposeapp.core.network.api.RecipesApiService
import com.ifedorov.recipecomposeapp.data.database.RecipesDatabase
import com.ifedorov.recipecomposeapp.data.model.CategoryDto
import com.ifedorov.recipecomposeapp.data.model.RecipeDto
import com.ifedorov.recipecomposeapp.data.model.toDto
import com.ifedorov.recipecomposeapp.data.model.toEntity
import com.ifedorov.recipecomposeapp.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val api: RecipesApiService,
    database: RecipesDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RecipesRepository {

    private val categoryDao = database.categoryDao()
    private val recipeDao = database.recipeDao()

    private val repositoryScope = CoroutineScope(SupervisorJob() + ioDispatcher)

    override fun getCategories(): Flow<List<CategoryDto>> {
        repositoryScope.launch {
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
        repositoryScope.launch {
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

    override fun getRecipe(recipeId: Int): Flow<RecipeDto?> {
        repositoryScope.launch {
            try {
                val recipeFromApi = api.getRecipe(recipeId)
                val categoryId = recipeFromApi.categoryIds.firstOrNull() ?: -1
                recipeDao.insertRecipes(listOf(recipeFromApi.toEntity(categoryId)))
            } catch (e: Exception) {
                Log.e("Repository", "Ошибка при получении рецепта из API", e)
            }
        }

        return recipeDao.getRecipeById(recipeId).map { recipeEntity ->
            recipeEntity?.toDto()
        }
    }

    override fun getRecipesByIds(recipeIds: List<Int>): Flow<List<RecipeDto>> {
        repositoryScope.launch {
            recipeIds.forEach { recipeId ->
                try {
                    val recipeFromApi = api.getRecipe(recipeId)
                    val categoryId = recipeFromApi.categoryIds.firstOrNull() ?: -1
                    recipeDao.insertRecipes(listOf(recipeFromApi.toEntity(categoryId)))
                } catch (e: Exception) {
                    Log.e("Repository", "Ошибка при получении избранного рецепта: $recipeId", e)
                }
            }
        }

        return recipeDao.getRecipesByIds(recipeIds).map { recipes ->
            recipes.map { it.toDto() }
        }
    }
}