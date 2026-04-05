package com.ifedorov.recipecomposeapp.features.recipes.presentation.model

import com.ifedorov.recipecomposeapp.core.utils.Constants.IMAGES_BASE_URL
import fixtures.RecipeTestFixtures.TEST_ID
import fixtures.RecipeTestFixtures.TEST_IMAGE_NAME
import fixtures.RecipeTestFixtures.TEST_IMAGE_URL
import fixtures.RecipeTestFixtures.TEST_INGREDIENTS
import fixtures.RecipeTestFixtures.TEST_METHOD
import fixtures.RecipeTestFixtures.TEST_SERVINGS
import fixtures.RecipeTestFixtures.TEST_TITLE
import fixtures.RecipeTestFixtures.createRecipeDto
import org.junit.Assert.assertEquals
import org.junit.Test

class RecipeDtoMapperTest {
    @Test
    fun `preserves full imageUrl starting with http`() {
        val dto = createRecipeDto(imageUrl = TEST_IMAGE_URL)
        val result = dto.toUiModel()

        assertEquals(TEST_IMAGE_URL, result.imageUrl)
    }

    @Test
    fun `prepends base url to relative imageUrl`() {
        val dto = createRecipeDto(imageUrl = TEST_IMAGE_NAME)
        val result = dto.toUiModel()

        assertEquals(IMAGES_BASE_URL + TEST_IMAGE_NAME, result.imageUrl)
    }

    @Test
    fun `maps DTO to UI model correctly`() {
        val dto = createRecipeDto()
        val result = dto.toUiModel(isFavorite = true)

        val expected = RecipeUiModel(
            id = TEST_ID,
            title = TEST_TITLE,
            imageUrl = IMAGES_BASE_URL + TEST_IMAGE_NAME,
            ingredients = TEST_INGREDIENTS.map { it.toUiModel() },
            method = TEST_METHOD,
            isFavorite = true,
            servings = TEST_SERVINGS
        )

        assertEquals(expected, result)
    }
}