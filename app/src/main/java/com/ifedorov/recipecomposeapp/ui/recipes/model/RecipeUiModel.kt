package com.ifedorov.recipecomposeapp.ui.recipes.model

import androidx.compose.runtime.Immutable
import com.ifedorov.recipecomposeapp.core.Constants
import com.ifedorov.recipecomposeapp.data.model.RecipeDto

@Immutable
data class RecipeUiModel(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val ingredients: List<IngredientUiModel>,
    val method: List<String>,
    val isFavorite: Boolean,
)

fun RecipeDto.toUiModel(isFavorite: Boolean = false) = RecipeUiModel(
    id = id,
    title = title,
    imageUrl = if (imageUrl.startsWith(prefix = "http", ignoreCase = true)) {
        imageUrl
    } else {
        Constants.ASSETS_URI_PREFIX + imageUrl
    },
    ingredients = ingredients.map { it.toUiModel() },
    method = method,
    isFavorite = isFavorite
)