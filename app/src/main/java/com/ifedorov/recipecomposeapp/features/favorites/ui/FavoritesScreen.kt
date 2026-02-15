package com.ifedorov.recipecomposeapp.features.favorites.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.datastore.FavoriteDataStoreManager
import com.ifedorov.recipecomposeapp.core.ui.components.ScreenHeader
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.features.recipes.ui.RecipeItem
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.toUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import kotlinx.coroutines.flow.map

@Composable
fun FavoritesScreen(
    repository: RecipesRepositoryStub,
    dataStoreManager: FavoriteDataStoreManager,
    modifier: Modifier = Modifier,
    onFavoriteRecipeClick: (Int) -> Unit
) {
    val favoritesFlow = remember(repository, dataStoreManager) {
        dataStoreManager.getFavoriteIdsFlow()
            .map { ids ->
                ids.mapNotNull { idString ->
                    val id = idString.toIntOrNull()
                    id?.let {
                        repository.getRecipeById(it)?.toUiModel()
                    }
                }
            }
    }

    val favorites by favoritesFlow.collectAsState(initial = emptyList())

    if (favorites.isEmpty()) {
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
    } else {
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
            items(items = favorites, key = { it.id }) { recipe ->
                RecipeItem(
                    recipe = recipe,
                    onRecipeClick = onFavoriteRecipeClick,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFavoritesScreen() {
    val context = LocalContext.current

    RecipeComposeAppTheme {
        FavoritesScreen(
            repository = RecipesRepositoryStub,
            dataStoreManager = FavoriteDataStoreManager(context),
            onFavoriteRecipeClick = {}
        )
    }
}