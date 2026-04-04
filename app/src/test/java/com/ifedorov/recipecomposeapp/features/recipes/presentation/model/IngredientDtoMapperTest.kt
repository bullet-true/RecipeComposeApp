package com.ifedorov.recipecomposeapp.features.recipes.presentation.model

import com.ifedorov.recipecomposeapp.data.model.IngredientDto
import org.junit.Assert.assertEquals

import org.junit.Test

class IngredientDtoMapperTest {
    @Test
    fun `when mapping ingredient dto then all fields are mapped correctly`() {
        val dto = createIngredientDto()
        val result = dto.toUiModel()

        assertEquals(NAME, result.name)
        assertEquals(QUANTITY, result.quantity)
        assertEquals(UNIT, result.unitOfMeasure)
    }

    private fun createIngredientDto(
        name: String = NAME,
        quantity: String = QUANTITY,
        unit: String = UNIT
    ) = IngredientDto(
        quantity = quantity,
        unitOfMeasure = unit,
        description = name
    )

    companion object {
        private const val NAME = "говяжий фарш"
        private const val QUANTITY = "0.5"
        private const val UNIT = "кг"
    }
}