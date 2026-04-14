package com.ifedorov.recipecomposeapp.di

import com.ifedorov.recipecomposeapp.data.model.CategoryDto
import com.ifedorov.recipecomposeapp.data.model.RecipeDto
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)

object TestRepositoryModule {

    @Provides
    @Singleton
    fun provideTestRecipesRepository(): RecipesRepository =
        object : RecipesRepository {
            override fun getCategories(): Flow<List<CategoryDto>> = flowOf(
                listOf(
                    CategoryDto(
                        id = TEST_CATEGORY_ID,
                        title = TEST_CATEGORY_TITLE,
                        description = TEST_CATEGORY_DESCRIPTION,
                        imageUrl = TEST_CATEGORY_IMAGE_URL
                    )
                )
            )

            override fun getRecipesByCategory(categoryId: Int): Flow<List<RecipeDto>> =
                flowOf(emptyList())

            override fun getRecipe(recipeId: Int): Flow<RecipeDto?> = flowOf(null)

            override fun getRecipesByIds(recipeIds: List<Int>): Flow<List<RecipeDto>> =
                flowOf(emptyList())
        }

    private const val TEST_CATEGORY_ID = 1
    private const val TEST_CATEGORY_TITLE = "Завтраки"
    private const val TEST_CATEGORY_DESCRIPTION = "Тестовое описание"
    private const val TEST_CATEGORY_IMAGE_URL = "breakfast.jpg"
}