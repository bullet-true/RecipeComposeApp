package com.ifedorov.recipecomposeapp.features.details.presentation.model

import com.ifedorov.recipecomposeapp.core.extensions.IngredientExtensions.scaled
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel

data class RecipeDetailsUiState(
    val recipe: RecipeUiModel? = null,
    val currentPortions: Int = 1,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false,
) {
    val scaledIngredients get() = recipe?.let { recipe ->
        val multiplier = currentPortions.toDouble() / recipe.servings.coerceAtLeast(1)
        recipe.ingredients.map { it.scaled(multiplier) }
    }.orEmpty()
}