package com.ifedorov.recipecomposeapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.datastore.FavoriteDataStoreManager
import com.ifedorov.recipecomposeapp.core.extensions.IngredientExtensions.scaled
import com.ifedorov.recipecomposeapp.core.ui.components.ScreenHeader
import com.ifedorov.recipecomposeapp.core.utils.ShareUtils
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.ui.recipes.model.RecipeUiModel
import com.ifedorov.recipecomposeapp.ui.recipes.model.toUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun RecipeDetailsScreen(
    recipeId: Int,
    modifier: Modifier = Modifier
) {
    var recipeUi by remember { mutableStateOf<RecipeUiModel?>(null) }

    LaunchedEffect(recipeId) {
        recipeUi = RecipesRepositoryStub.getRecipeById(recipeId)?.toUiModel()
    }

    val recipe = recipeUi
    if (recipe == null) {
        Text(
            text = stringResource(R.string.recipe_not_found),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    } else {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        val favoriteDataStoreManager = remember(context) { FavoriteDataStoreManager(context) }
        val isFavorite by favoriteDataStoreManager.isFavoriteFlow(recipe.id)
            .collectAsState(initial = false)

        val backgroundImage = rememberAsyncImagePainter(recipe.imageUrl)
        var currentPortions by rememberSaveable(recipe.id) { mutableIntStateOf(1) }

        val scaledIngredients = remember(currentPortions, recipe.ingredients) {
            val multiplier = currentPortions.toDouble() / recipe.servings
            recipe.ingredients.map { it.scaled(multiplier) }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                ScreenHeader(
                    title = recipe.title,
                    backgroundImage = backgroundImage,
                    showShareButton = true,
                    onShareClick = {
                        ShareUtils.shareRecipe(
                            context = context,
                            recipeId = recipe.id,
                            recipeTitle = recipe.title
                        )
                    },
                    showFavoriteButton = true,
                    isFavorite = isFavorite,
                    onFavoriteToggle = {
                        coroutineScope.launch {
                            if (isFavorite) {
                                favoriteDataStoreManager.removeFavorite(recipe.id)
                            } else {
                                favoriteDataStoreManager.addFavorite(recipe.id)
                            }
                        }
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Text(
                    text = stringResource(R.string.ingredients).uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(6.dp))
            }
            item {
                PortionsSelector(
                    currentPortions = currentPortions,
                    onPortionsChange = { newValue ->
                        currentPortions = newValue
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                IngredientsList(
                    ingredients = scaledIngredients,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Text(
                    text = stringResource(R.string.method).uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                InstructionsList(
                    instructions = recipe.method,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipeDetailsScreen() {
    RecipeComposeAppTheme {
        RecipeDetailsScreen(
            recipeId = 0
        )
    }
}