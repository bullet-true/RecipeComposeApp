package com.ifedorov.recipecomposeapp.core.ui.navigation

import android.net.Uri
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_ID
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_IMAGE_URL
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_TITLE
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_RECIPE_ID

sealed class Destination(val route: String) {
    object Categories : Destination("categories")
    object Favorites : Destination("favorites")

    object Recipes :
        Destination("recipes/{$PARAM_CATEGORY_ID}/{$PARAM_CATEGORY_TITLE}/{$PARAM_CATEGORY_IMAGE_URL}") {

        fun createRoute(categoryId: Int, categoryTitle: String, categoryImageUrl: String): String {
            val categoryTitleEncode = Uri.encode(categoryTitle)
            val categoryImageUrlEncode = Uri.encode(categoryImageUrl)

            return "recipes/$categoryId/$categoryTitleEncode/$categoryImageUrlEncode"
        }
    }

    object RecipeDetails : Destination("recipe/{$PARAM_RECIPE_ID}") {
        fun createRoute(recipeId: Int) = "recipe/$recipeId"
    }
}