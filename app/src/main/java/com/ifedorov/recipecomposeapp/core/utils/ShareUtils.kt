package com.ifedorov.recipecomposeapp.core.utils

import android.content.Context
import android.content.Intent
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.ui.navigation.DeepLinkRoutes

object ShareUtils {
    fun shareRecipe(context: Context, recipeId: Int, recipeTitle: String) {
        val shareLink = DeepLinkRoutes.createRecipeDeepLink(recipeId)
        val shareText = context.getString(R.string.share_recipe_text, recipeTitle, shareLink)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }

        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_recipe)))
    }
}