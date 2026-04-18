package com.ifedorov.recipecomposeapp.e2e

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ifedorov.recipecomposeapp.MainActivity
import com.ifedorov.recipecomposeapp.features.categories.ui.CategoriesComposeScreen
import com.ifedorov.recipecomposeapp.features.recipes.ui.RecipesComposeScreen
import com.kaspersky.components.composesupport.config.withComposeSupport
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoriesE2ETest : TestCase(
    kaspressoBuilder = Kaspresso.Builder.withComposeSupport()
) {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun categoriesScreenLoadsContent() = run {
        step("Открыть приложение и дождаться загрузки категорий") {
            onComposeScreen<CategoriesComposeScreen>(composeTestRule) {
                categoriesGrid {
                    assertIsDisplayed()
                }
            }
        }
    }

    @Test
    fun clickingCategoryOpensRecipesScreen() = run {
        step("Дождаться загрузки категорий") {
            onComposeScreen<CategoriesComposeScreen>(composeTestRule) {
                categoriesGrid {
                    assertIsDisplayed()
                }
            }
        }

        step("Нажать на первую категорию") {
            onComposeScreen<CategoriesComposeScreen>(composeTestRule) {
                categoryItem {
                    performClick()
                }
            }
        }

//        step("Проверить, что открылся экран рецептов и показан loading") {
//            onComposeScreen<RecipesComposeScreen>(composeTestRule) {
//                loadingIndicator {
//                    assertIsDisplayed()
//                }
//            }
//        }

        step("Проверить, что открылся экран рецептов") {
            onComposeScreen<RecipesComposeScreen>(composeTestRule) {
                assertIsDisplayed()
            }
        }
    }
}