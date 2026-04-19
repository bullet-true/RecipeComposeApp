package com.ifedorov.recipecomposeapp.features.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.ui.components.ScreenHeader
import com.ifedorov.recipecomposeapp.core.utils.ShareUtils
import com.ifedorov.recipecomposeapp.features.details.presentation.RecipeDetailsViewModel
import com.ifedorov.recipecomposeapp.features.details.presentation.model.RecipeDetailsUiState
import com.ifedorov.recipecomposeapp.features.details.ui.components.IngredientsList
import com.ifedorov.recipecomposeapp.features.details.ui.components.InstructionsList
import com.ifedorov.recipecomposeapp.features.details.ui.components.PortionsSelector
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import java.util.Locale

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailsViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    RecipeDetailsContent(
        uiState = uiState,
        modifier = modifier,
        onShareClick = {
            uiState.recipe?.let { recipe ->
                ShareUtils.shareRecipe(
                    context = context,
                    recipeId = recipe.id,
                    recipeTitle = recipe.title
                )
            }
        },
        onFavoriteToggle = { viewModel.toggleFavorite() },
        onPortionsChange = { newValue ->
            viewModel.updatePortions(newValue)
        }
    )
}

@Composable
fun RecipeDetailsContent(
    uiState: RecipeDetailsUiState,
    modifier: Modifier = Modifier,
    onShareClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onPortionsChange: (Int) -> Unit
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.error != null -> {
            Box(
                modifier = modifier
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

        uiState.recipe == null -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.recipe_not_found),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        else -> {
            val recipe = uiState.recipe
            val backgroundImage = rememberAsyncImagePainter(recipe.imageUrl)

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    ScreenHeader(
                        title = recipe.title,
                        backgroundImage = backgroundImage,
                        showShareButton = true,
                        onShareClick = onShareClick,
                        showFavoriteButton = true,
                        isFavorite = uiState.isFavorite,
                        onFavoriteToggle = onFavoriteToggle
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
                        currentPortions = uiState.currentPortions,
                        onPortionsChange = onPortionsChange,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    IngredientsList(
                        ingredients = uiState.scaledIngredients,
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
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipeDetailsContent() {
    val recipe = RecipeUiModel(
        id = 1,
        title = "Бургер",
        imageUrl = "burger.jpg",
        ingredients = listOf(
            IngredientUiModel(
                name = "Говяжий фарш",
                quantity = "500",
                unitOfMeasure = "г"
            )
        ),
        method = listOf("Пункт 1", "Пункт 2", "Пункт 3"),
        isFavorite = false,
        servings = 1
    )

    RecipeComposeAppTheme {
        RecipeDetailsContent(
            uiState = RecipeDetailsUiState(
                recipe = recipe,
                currentPortions = 3,
                scaledIngredients = recipe.ingredients,
                isLoading = false,
                error = null,
                isFavorite = true
            ),
            onShareClick = {},
            onFavoriteToggle = {},
            onPortionsChange = {}
        )
    }
}