package com.ifedorov.recipecomposeapp.core.ui.navigation

import com.ifedorov.recipecomposeapp.core.Constants.PARAM_RECIPE_ID

sealed class Destination(val route: String) {
    object Categories: Destination("categories")
    object Favorites : Destination("favorites")
    object Recipes: Destination("recipes/{categoryId}/{categoryTitle}") {
        fun createRoute(categoryId: Int, categoryTitle: String) = "recipes/$categoryId/$categoryTitle"
    }
    object RecipeDetails: Destination("recipe/{$PARAM_RECIPE_ID}") {
        fun createRoute(recipeId: Int) = "recipe/$recipeId"
    }
}