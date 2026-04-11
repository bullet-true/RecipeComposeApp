package com.ifedorov.recipecomposeapp.features.recipes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.ui.TestTags
import com.ifedorov.recipecomposeapp.core.ui.components.ScreenHeader
import com.ifedorov.recipecomposeapp.features.recipes.presentation.RecipesViewModel
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.RecipesUiState
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun RecipesScreen(
    viewModel: RecipesViewModel,
    modifier: Modifier = Modifier,
    onRecipeClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    RecipesContent(
        uiState = uiState,
        modifier = modifier,
        onRecipeClick = onRecipeClick
    )
}

@Composable
fun RecipesContent(
    uiState: RecipesUiState,
    modifier: Modifier = Modifier,
    onRecipeClick: (Int) -> Unit
) {
    val headerImage = rememberAsyncImagePainter(uiState.categoryImageUrl)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            title = uiState.categoryTitle,
            backgroundImage = headerImage
        )

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.testTag(TestTags.LOADING_STATE)
                    )
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.downloading_error, uiState.error ?: ""),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag(TestTags.ERROR_STATE)
                    )
                }
            }

            uiState.isRecipesListEmpty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.downloading_error_or_list_is_empty),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag(TestTags.EMPTY_STATE)
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items = uiState.recipes, key = { it.id }) { recipe ->
                        RecipeItem(
                            recipe = recipe,
                            onRecipeClick = onRecipeClick,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipesScreen() {
    RecipeComposeAppTheme {
        RecipesContent(
            uiState = RecipesUiState(
                categoryTitle = "Классический гамбургер",
                recipes = listOf(
                    RecipeUiModel(
                        id = 1,
                        title = "Классический бургер",
                        imageUrl = "burger-hamburger.jpg",
                        ingredients = listOf(
                            IngredientUiModel(
                                name = "Фарш говяжий",
                                quantity = "150",
                                unitOfMeasure = "г"
                            )
                        ),
                        method = listOf("Разрезать булочку", "Обжарить котлету"),
                        isFavorite = false,
                        servings = 1
                    ),
                    RecipeUiModel(
                        id = 2,
                        title = "Чизбургер",
                        imageUrl = "burger-cheeseburger.jpg",
                        ingredients = listOf(
                            IngredientUiModel(
                                name = "Сыр чеддер",
                                quantity = "1",
                                unitOfMeasure = "ломтик"
                            )
                        ),
                        method = listOf("Разрезать булочку", "Обжарить котлету"),
                        isFavorite = false,
                        servings = 1
                    )
                )
            ),
            onRecipeClick = { }
        )
    }
}