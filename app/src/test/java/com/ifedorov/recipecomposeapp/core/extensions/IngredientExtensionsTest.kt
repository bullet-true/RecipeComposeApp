package com.ifedorov.recipecomposeapp.core.extensions

import com.ifedorov.recipecomposeapp.core.extensions.IngredientExtensions.scaled
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import org.junit.Assert.assertEquals

import org.junit.Test

class IngredientExtensionsTest {
    @Test
    fun `scaled should multiply quantity correctly`() {
        val ingredient = createIngredientUiModel(quantity = "2.5")
        val result = ingredient.scaled(2.0)

        assertEquals("5", result.quantity)
        assertEquals(ingredient.name, result.name)
        assertEquals(ingredient.unitOfMeasure, result.unitOfMeasure)
    }

    @Test
    fun `scaled should round half up correctly`() {
        val ingredient = createIngredientUiModel(quantity = "1.25")
        val result = ingredient.scaled(1.5)

        assertEquals("1.9", result.quantity)
    }

    @Test
    fun `scaled should strip trailing zeros`() {
        val ingredient = createIngredientUiModel(quantity = "2.0")
        val result = ingredient.scaled(1.0)

        assertEquals("2", result.quantity)
    }

    @Test
    fun `should return original when quantity is not a number`() {
        val ingredient = createIngredientUiModel(quantity = "abc")
        val result = ingredient.scaled(2.0)

        assertEquals(ingredient, result)
    }

    @Test
    fun `should round down correctly`() {
        val ingredient = createIngredientUiModel(quantity = "1.21")
        val result = ingredient.scaled(1.0)

        assertEquals("1.2", result.quantity)
    }

    @Test
    fun `should not change value without trailing zeros`() {
        val ingredient = createIngredientUiModel(quantity = "2.5")
        val result = ingredient.scaled(1.0)

        assertEquals("2.5", result.quantity)
    }

    private fun createIngredientUiModel(
        name: String = "Test",
        quantity: String = "1.0",
        unit: String = "шт"
    ) = IngredientUiModel(
        name = name,
        quantity = quantity,
        unitOfMeasure = unit
    )
}