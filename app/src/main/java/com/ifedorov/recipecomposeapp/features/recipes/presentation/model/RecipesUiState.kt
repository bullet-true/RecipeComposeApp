package com.ifedorov.recipecomposeapp.features.recipes.presentation.model

data class RecipesUiState(
    val categoryId: Int = 0,
    val categoryTitle: String = "",
    val categoryImageUrl: String = "",
    val recipes: List<RecipeUiModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    val isRecipesListEmpty get() = recipes.isEmpty()
}