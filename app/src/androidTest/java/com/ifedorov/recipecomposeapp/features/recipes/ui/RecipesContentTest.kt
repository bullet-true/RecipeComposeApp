package com.ifedorov.recipecomposeapp.features.recipes.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.ifedorov.recipecomposeapp.core.ui.TestTags
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.RecipesUiState
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import org.junit.Rule
import org.junit.Test

class RecipesContentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun showsLoadingState() {
        composeTestRule.setContent {
            RecipeComposeAppTheme {
                RecipesContent(
                    uiState = RecipesUiState(isLoading = true),
                    onRecipeClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag(TestTags.LOADING_STATE)
            .assertIsDisplayed()
    }

    @Test
    fun showsErrorState() {
        composeTestRule.setContent {
            RecipeComposeAppTheme {
                RecipesContent(
                    uiState = RecipesUiState(error = TEST_ERROR),
                    onRecipeClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag(TestTags.ERROR_STATE)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(TEST_ERROR, substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun showsEmptyState() {
        composeTestRule.setContent {
            RecipeComposeAppTheme {
                RecipesContent(
                    uiState = RecipesUiState(),
                    onRecipeClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithTag(TestTags.EMPTY_STATE)
            .assertIsDisplayed()
    }

    @Test
    fun displaysRecipeList() {
        val recipes = listOf(
            createRecipe(TEST_RECIPE_ID_1, TEST_RECIPE_TITLE_1),
            createRecipe(TEST_RECIPE_ID_2, TEST_RECIPE_TITLE_2)
        )

        composeTestRule.setContent {
            RecipeComposeAppTheme {
                RecipesContent(
                    uiState = RecipesUiState(recipes = recipes),
                    onRecipeClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText(TEST_RECIPE_TITLE_1_UPPER)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(TEST_RECIPE_TITLE_2_UPPER)
            .assertIsDisplayed()
    }

    private fun createRecipe(
        id: Int, title: String
    ) = RecipeUiModel(
        id = id,
        title = title,
        imageUrl = TEST_IMAGE_URL,
        ingredients = listOf(
            IngredientUiModel(
                name = TEST_INGREDIENT,
                quantity = TEST_QUANTITY,
                unitOfMeasure = TEST_UNIT
            )
        ),
        method = listOf(TEST_METHOD),
        isFavorite = false,
        servings = 1
    )

    companion object {
        private const val TEST_ERROR = "Network error"

        private const val TEST_RECIPE_ID_1 = 1
        private const val TEST_RECIPE_ID_2 = 2
        private const val TEST_RECIPE_TITLE_1 = "Бургер"
        private const val TEST_RECIPE_TITLE_1_UPPER = "БУРГЕР"
        private const val TEST_RECIPE_TITLE_2 = "Салат"
        private const val TEST_RECIPE_TITLE_2_UPPER = "САЛАТ"
        private const val TEST_IMAGE_URL = "image.jpg"
        private const val TEST_INGREDIENT = "Ингредиент 1"
        private const val TEST_QUANTITY = "2"
        private const val TEST_UNIT = "шт"
        private const val TEST_METHOD = "Пункт 1"
    }
}