package com.ifedorov.recipecomposeapp.core.extensions

import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import java.math.RoundingMode

object IngredientExtensions {
    fun IngredientUiModel.scaled(multiplier: Double): IngredientUiModel {
        val scaled = quantity
            .toBigDecimalOrNull()
            ?.multiply(multiplier.toBigDecimal())
            ?.setScale(1, RoundingMode.HALF_UP)
            ?.stripTrailingZeros()
            ?.toPlainString()
            ?: return this

        return copy(quantity = scaled)
    }
}