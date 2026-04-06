package com.ifedorov.recipecomposeapp.features.categories.presentation.model

import com.ifedorov.recipecomposeapp.core.utils.Constants.IMAGES_BASE_URL
import fixtures.CategoryTestFixtures.EMPTY_TITLE
import fixtures.CategoryTestFixtures.TEST_DESCRIPTION
import fixtures.CategoryTestFixtures.TEST_IMAGE_NAME
import fixtures.CategoryTestFixtures.TEST_LONG_DESCRIPTION
import fixtures.CategoryTestFixtures.TEST_TITLE
import fixtures.CategoryTestFixtures.createCategoryDto
import org.junit.Assert.assertEquals
import org.junit.Test

class CategoryDtoMapperTest {

    @Test
    fun `mapper maps empty title correctly`() {
        val dto = createCategoryDto(title = EMPTY_TITLE)
        val result = dto.toUiModel()

        assertEquals(EMPTY_TITLE, result.title)
        assertEquals(TEST_DESCRIPTION, result.description)
        assertEquals(IMAGES_BASE_URL + TEST_IMAGE_NAME, result.imageUrl)
    }

    @Test
    fun `mapper preserves very long description`() {
        val dto = createCategoryDto(description = TEST_LONG_DESCRIPTION)
        val result = dto.toUiModel()

        assertEquals(TEST_TITLE, result.title)
        assertEquals(TEST_LONG_DESCRIPTION, result.description)
        assertEquals(IMAGES_BASE_URL + TEST_IMAGE_NAME, result.imageUrl)
    }
}