package com.ifedorov.recipecomposeapp.core.ui.navigation

import com.ifedorov.recipecomposeapp.core.utils.Constants.DEEP_LINK_BASE_URL

object DeepLinkRoutes {
    fun createRecipeDeepLink(recipeId: Int) = "$DEEP_LINK_BASE_URL/recipe/$recipeId"
}