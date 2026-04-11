package com.ifedorov.recipecomposeapp.features.categories.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ifedorov.recipecomposeapp.core.ui.TestTags
import com.ifedorov.recipecomposeapp.features.categories.presentation.model.CategoriesUiState
import com.ifedorov.recipecomposeapp.features.categories.presentation.model.CategoryUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoriesContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun displaysCategories() {
        val category = createCategory()

        composeTestRule.setContent {
            RecipeComposeAppTheme {
                CategoriesContent(
                    uiState = CategoriesUiState(categories = listOf(category)),
                    onCategoryClick = { _, _, _ -> }
                )
            }
        }

        composeTestRule
            .onNodeWithText(TEST_TITLE_UPPERCASE)
            .assertIsDisplayed()
    }

    @Test
    fun clickingCategoryNavigatesToRecipes() {
        val category = createCategory()
        var clickedId: Int? = null

        composeTestRule.setContent {
            RecipeComposeAppTheme {
                CategoriesContent(
                    uiState = CategoriesUiState(categories = listOf(category)),
                    onCategoryClick = { id, _, _ -> clickedId = id }
                )
            }
        }

        composeTestRule
            .onNodeWithText(TEST_TITLE_UPPERCASE)
            .performClick()

        assertEquals(TEST_ID, clickedId)
    }

    @Test
    fun showsLoadingState() {
        composeTestRule.setContent {
            RecipeComposeAppTheme {
                CategoriesContent(
                    uiState = CategoriesUiState(isLoading = true),
                    onCategoryClick = { id, _, _ -> }
                )
            }
        }

        composeTestRule
            .onNodeWithTag(TestTags.LOADING_STATE)
            .assertIsDisplayed()
    }

    private fun createCategory() = CategoryUiModel(
        id = TEST_ID,
        title = TEST_TITLE,
        description = TEST_DESCRIPTION,
        imageUrl = TEST_IMAGE_URL
    )

    companion object {
        private const val TEST_ID = 1
        private const val TEST_TITLE = "Завтраки"
        private const val TEST_TITLE_UPPERCASE = "ЗАВТРАКИ"
        private const val TEST_DESCRIPTION = "Описание"
        private const val TEST_IMAGE_URL = "breakfast.jpg"
    }
}