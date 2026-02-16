package com.ifedorov.recipecomposeapp.features.recipes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.ifedorov.recipecomposeapp.core.ui.components.ScreenHeader
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.toUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import kotlinx.coroutines.delay

@Composable
fun RecipesScreen(
    categoryId: Int,
    categoryTitle: String,
    categoryImageUrl: String,
    modifier: Modifier = Modifier,
    onRecipeClick: (Int) -> Unit
) {
    var recipes by remember { mutableStateOf<List<RecipeUiModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val headerImage = rememberAsyncImagePainter(categoryImageUrl)

    LaunchedEffect(categoryId) {
        isLoading = true
        delay(500)

        try {
            recipes = RecipesRepositoryStub.getRecipesByCategoryId(categoryId).map { it.toUiModel() }
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ScreenHeader(
                title = categoryTitle,
                backgroundImage = headerImage
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ScreenHeader(
                    title = categoryTitle,
                    backgroundImage = headerImage
                )
            }
            items(items = recipes, key = { it.id }) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    onRecipeClick = onRecipeClick,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipesScreen() {
    RecipeComposeAppTheme {
        RecipesScreen(
            categoryId = 0,
            categoryTitle = "Бургеры",
            categoryImageUrl = "file:///android_asset/burger.png",
            onRecipeClick = {}
        )
    }
}