package com.ifedorov.recipecomposeapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ifedorov.recipecomposeapp.core.Constants.KEY_RECIPE_OBJECT
import com.ifedorov.recipecomposeapp.core.ui.navigation.BottomNavigation
import com.ifedorov.recipecomposeapp.core.ui.navigation.Destination
import com.ifedorov.recipecomposeapp.ui.categories.CategoriesScreen
import com.ifedorov.recipecomposeapp.ui.details.RecipeDetailsScreen
import com.ifedorov.recipecomposeapp.ui.favorites.FavoritesScreen
import com.ifedorov.recipecomposeapp.ui.recipes.RecipesScreen
import com.ifedorov.recipecomposeapp.ui.recipes.model.RecipeUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun RecipesApp() {
    val navController = rememberNavController()

    RecipeComposeAppTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = {
                        val popped = navController.popBackStack(
                            route = Destination.Categories.route,
                            inclusive = false
                        )

                        if (!popped) {
                            navController.navigate(Destination.Categories.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    onFavoriteClick = {
                        navController.navigate(Destination.Favorites.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
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
                            backStackEntry.arguments?.getString("categoryTitle") ?: "Unknown title"

                        RecipesScreen(
                            categoryId = categoryId,
                            categoryTitle = categoryTitle,
                            onRecipeClick = { recipeId, recipe ->
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    KEY_RECIPE_OBJECT,
                                    recipe
                                )
                                navController.navigate(
                                    Destination.RecipeDetails.createRoute(recipeId)
                                )
                            },
                        )
                    }
                    composable(
                        route = Destination.RecipeDetails.route,
                        arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
                    ) {
                        val savedStateHandle =
                            navController.previousBackStackEntry?.savedStateHandle

                        val recipe = savedStateHandle?.get<RecipeUiModel>(KEY_RECIPE_OBJECT)

                        if (recipe != null) {
                            RecipeDetailsScreen(recipe)
                        } else {
                            Text("Рецепт не найден")
                        }
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