package com.ifedorov.recipecomposeapp.features.favorites.ui

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.ui.components.ScreenHeader
import com.ifedorov.recipecomposeapp.data.model.CategoryDto
import com.ifedorov.recipecomposeapp.data.model.RecipeDto
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import com.ifedorov.recipecomposeapp.features.favorites.presentation.FavoritesViewModel
import com.ifedorov.recipecomposeapp.features.recipes.ui.RecipeItem
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    modifier: Modifier = Modifier,
    onFavoriteRecipeClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.error != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.downloading_error, uiState.error ?: ""),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        uiState.favoriteRecipes.isEmpty() -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                ScreenHeader(
                    title = stringResource(R.string.favorites),
                    backgroundImage = painterResource(R.drawable.bcg_favorites)
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.empty_favorites_text),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    ScreenHeader(
                        title = stringResource(R.string.favorites),
                        backgroundImage = painterResource(R.drawable.bcg_favorites)
                    )
                }
                items(items = uiState.favoriteRecipes, key = { it.id }) { recipe ->
                    RecipeItem(
                        recipe = recipe,
                        onRecipeClick = onFavoriteRecipeClick,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFavoritesScreen() {
    val context = LocalContext.current
    val application = context.applicationContext as android.app.Application

    val fakeRepository: RecipesRepository = object : RecipesRepository {
        override suspend fun getCategories() = emptyList<CategoryDto>()
        override suspend fun getRecipesByCategory(categoryId: Int) = emptyList<RecipeDto>()
        override suspend fun getRecipe(recipeId: Int) = throw NotImplementedError()
    }

    val fakeViewModel = FavoritesViewModel(
        application = application,
        repository = fakeRepository
    )

    RecipeComposeAppTheme {
        FavoritesScreen(
            viewModel = fakeViewModel,
            onFavoriteRecipeClick = {}
        )
    }
}