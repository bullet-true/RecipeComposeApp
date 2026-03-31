package com.ifedorov.recipecomposeapp.features.recipes.presentation.model

import com.ifedorov.recipecomposeapp.core.utils.Constants.IMAGES_BASE_URL
import com.ifedorov.recipecomposeapp.data.model.IngredientDto
import com.ifedorov.recipecomposeapp.data.model.RecipeDto
import org.junit.Assert.assertEquals
import org.junit.Test

class RecipeDtoMapperTest {
    @Test
    fun `convert DTO to UI model when image url starts with http`() {
        val dto = createRecipeDto(imageUrl = IMAGE_URL)
        val result = dto.toUiModel()

        assertEquals(IMAGE_URL, result.imageUrl)
    }

    @Test
    fun `convert DTO to UI model when image url starts without http`() {
        val dto = createRecipeDto(imageUrl = IMAGE_NAME)
        val result = dto.toUiModel()

        assertEquals(IMAGES_BASE_URL + IMAGE_NAME, result.imageUrl)
    }

    @Test
    fun `when mapping recipe dto then all fields are mapped correctly`() {
        val dto = createRecipeDto()
        val result = dto.toUiModel(isFavorite = true)

        val expected = RecipeUiModel(
            id = ID,
            title = TITLE,
            imageUrl = IMAGES_BASE_URL + IMAGE_NAME,
            ingredients = INGREDIENTS.map { it.toUiModel() },
            method = METHOD,
            isFavorite = true,
            servings = 1
        )

        assertEquals(expected, result)
    }

    private fun createRecipeDto(imageUrl: String = IMAGE_NAME) = RecipeDto(
        id = ID,
        title = TITLE,
        ingredients = INGREDIENTS,
        method = METHOD,
        imageUrl = imageUrl,
        categoryIds = listOf(0)
    )

    companion object {
        private const val ID = 1
        private const val TITLE = "Burger"
        private const val IMAGE_URL = "https://images/burger.png"
        private const val IMAGE_NAME = "burger.png"
        private val INGREDIENTS = listOf(
            IngredientDto("0.5", "кг", "говяжий фарш"),
            IngredientDto("1.0", "шт", "луковица, мелко нарезанная")
        )

        private val METHOD = listOf("Пункт 1", "Пункт 2")
    }
}