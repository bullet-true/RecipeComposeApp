package com.ifedorov.recipecomposeapp.features.categories.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.ifedorov.recipecomposeapp.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CategoriesScreenHiltTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun categoriesFromTestRepositoryAreDisplayed() {
        hiltRule.inject()

        composeRule
            .onNodeWithText(TEST_CATEGORY)
            .assertIsDisplayed()
    }

    companion object {
        const val TEST_CATEGORY = "ЗАВТРАКИ"
    }
}