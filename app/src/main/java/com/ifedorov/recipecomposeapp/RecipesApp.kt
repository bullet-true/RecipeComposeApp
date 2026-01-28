package com.ifedorov.recipecomposeapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ifedorov.recipecomposeapp.core.ui.navigation.BottomNavigation
import com.ifedorov.recipecomposeapp.core.ui.navigation.Destination
import com.ifedorov.recipecomposeapp.ui.categories.CategoriesScreen
import com.ifedorov.recipecomposeapp.ui.favorites.FavoritesScreen
import com.ifedorov.recipecomposeapp.ui.recipes.RecipesScreen
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun RecipesApp() {
    val navController = rememberNavController()

    RecipeComposeAppTheme {
        Scaffold(
            bottomBar = {
                val backStackEntry = navController.currentBackStackEntryAsState().value
                val currentRoute = backStackEntry?.destination?.route

                BottomNavigation(
                    onCategoriesClick = {
                        if (currentRoute != Destination.Categories.route) {
                            navController.navigate(Destination.Categories.route) {
                                restoreState = true
                            }
                        }
                    },
                    onFavoriteClick = {
                        if (currentRoute != Destination.Favorites.route) {
                            navController.navigate(Destination.Favorites.route) {
                                restoreState = true
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Destination.Categories.route
                ) {
                    composable(Destination.Categories.route) {
                        CategoriesScreen(
                            onCategoryClick = { id, title ->
                                navController.navigate(Destination.Recipes.createRoute(id, title))
                            }
                        )
                    }
                    composable(Destination.Favorites.route) {
                        FavoritesScreen()
                    }
                    composable(
                        route = Destination.Recipes.route,
                        arguments = listOf(
                            navArgument("categoryId") { type = NavType.IntType },
                            navArgument("categoryTitle") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val categoryId = backStackEntry.arguments?.getInt("categoryId") ?: 0
                        val categoryTitle =
                            backStackEntry.arguments?.getString("categoryTitle") ?: "Unknow title"

                        RecipesScreen(
                            categoryId = categoryId,
                            categoryTitle = categoryTitle,
                            onRecipeClick = { },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = PIXEL_7)
@Composable
private fun PreviewRecipesApp() {
    RecipesApp()
}