package com.ifedorov.recipecomposeapp.ui.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.ui.components.ScreenHeader
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.ui.recipes.model.RecipeUiModel
import com.ifedorov.recipecomposeapp.ui.recipes.model.toUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import kotlinx.coroutines.delay

@Composable
fun RecipesScreen(
    categoryId: Int,
    categoryTitle: String,
    modifier: Modifier = Modifier,
    onRecipeClick: (Int) -> Unit
) {
    var recipes by remember { mutableStateOf<List<RecipeUiModel>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(categoryId) {
        isLoading = true
        delay(2000)
        try {
            recipes = RecipesRepositoryStub.getRecipesByCategoryId(categoryId).map { it.toUiModel() }
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            title = categoryTitle,
            backgroundImage = R.drawable.img_error
        )

        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = recipes, key = { it.id }) { recipe ->
                    RecipeItem(
                        recipe = recipe,
                        onRecipeClick = onRecipeClick,
                    )
                }
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
            onRecipeClick = {}
        )
    }
}