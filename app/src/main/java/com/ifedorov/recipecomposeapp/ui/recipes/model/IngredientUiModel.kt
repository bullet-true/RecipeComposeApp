package com.ifedorov.recipecomposeapp.ui.recipes.model

import androidx.compose.runtime.Immutable
import com.ifedorov.recipecomposeapp.data.model.IngredientDto

@Immutable
data class IngredientUiModel(
    val name: String,
    val amount: String,
)

fun IngredientDto.toUiModel() = IngredientUiModel(
    name = description,
    amount = if (unitOfMeasure.isNotBlank()) "$quantity $unitOfMeasure" else quantity
)