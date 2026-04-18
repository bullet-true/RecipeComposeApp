package com.ifedorov.recipecomposeapp.features.recipes.ui

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import com.ifedorov.recipecomposeapp.core.ui.TestTags
import io.github.kakaocup.compose.node.element.ComposeScreen
import io.github.kakaocup.compose.node.element.KNode

class RecipesComposeScreen(
    semanticsProvider: SemanticsNodeInteractionsProvider
) : ComposeScreen<RecipesComposeScreen>(
    semanticsProvider = semanticsProvider,
    viewBuilderAction = { hasTestTag(TestTags.RECIPES_SCREEN) }
) {
    val loadingIndicator: KNode = child { hasTestTag(TestTags.LOADING_STATE) }
    val emptyState: KNode = child { hasTestTag(TestTags.EMPTY_STATE) }
}