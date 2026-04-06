package com.ifedorov.recipecomposeapp.features.recipes.presentation.model

import fixtures.RecipeTestFixtures.TEST_DESCRIPTION
import fixtures.RecipeTestFixtures.TEST_QUANTITY
import fixtures.RecipeTestFixtures.TEST_UNIT_OF_MEASURE
import fixtures.RecipeTestFixtures.createIngredientDto
import org.junit.Assert.assertEquals

import org.junit.Test

class IngredientDtoMapperTest {
    @Test
    fun `maps DTO to UI model correctly`() {
        val dto = createIngredientDto()
        val result = dto.toUiModel()

        assertEquals(TEST_DESCRIPTION, result.name)
        assertEquals(TEST_QUANTITY, result.quantity)
        assertEquals(TEST_UNIT_OF_MEASURE, result.unitOfMeasure)
    }
}