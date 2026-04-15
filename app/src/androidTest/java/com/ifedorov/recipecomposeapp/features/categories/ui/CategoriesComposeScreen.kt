package com.ifedorov.recipecomposeapp.features.categories.ui

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.ifedorov.recipecomposeapp.core.ui.TestTags
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode


class CategoriesComposeScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<CategoriesComposeScreen>(
    semanticsProvider = semanticsProvider,
    viewBuilderAction = { hasTestTag(TestTags.CATEGORIES_SCREEN) }
) {
    val loadingIndicator: KNode = child { hasTestTag(TestTags.LOADING_STATE) }
    val categoriesGrid: KNode = child { hasTestTag(TestTags.CATEGORIES_GRID) }
    val categoryItem: KNode = child { hasTestTag(TestTags.CATEGORY_ITEM) }
}