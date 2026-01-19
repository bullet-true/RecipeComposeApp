package com.ifedorov.recipecomposeapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7
import androidx.compose.ui.tooling.preview.Preview
import com.ifedorov.recipecomposeapp.core.ui.navigation.BottomNavigation
import com.ifedorov.recipecomposeapp.ui.categories.CategoriesScreen
import com.ifedorov.recipecomposeapp.ui.favorites.FavoritesScreen
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun RecipesApp() {
    var currentScreen by remember { mutableStateOf(ScreenId.CATEGORIES) }

    RecipeComposeAppTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = {
                        currentScreen = ScreenId.CATEGORIES
                    },
                    onFavoriteClick = {
                        currentScreen = ScreenId.FAVORITES
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when (currentScreen) {
                    ScreenId.CATEGORIES -> {
                        CategoriesScreen()
                    }

                    ScreenId.FAVORITES -> {
                        FavoritesScreen()
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