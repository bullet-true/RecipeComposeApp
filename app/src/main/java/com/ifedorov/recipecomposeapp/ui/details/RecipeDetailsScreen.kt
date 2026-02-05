package com.ifedorov.recipecomposeapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.extensions.IngredientExtensions.scaled
import com.ifedorov.recipecomposeapp.core.ui.components.ScreenHeader
import com.ifedorov.recipecomposeapp.core.utils.ShareUtils
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.ui.recipes.model.RecipeUiModel
import com.ifedorov.recipecomposeapp.ui.recipes.model.toUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
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
            text = "Рецепт не найден",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    } else {
        val context = LocalContext.current
        val backgroundImage = rememberAsyncImagePainter(recipe.imageUrl)
        var currentPortions by remember { mutableIntStateOf(1) }

        val scaledIngredients = remember(currentPortions, recipe.ingredients, recipe.servings) {
            val multiplier = currentPortions.toDouble() / recipe.servings

            recipe.ingredients.map { it.scaled(multiplier) }
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
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
                }
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    Text(
                        text = stringResource(R.string.ingredients).uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
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
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    IngredientsList(scaledIngredients)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Text(
                        text = stringResource(R.string.method).uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    InstructionsList(recipe.method)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipeDetailsScreen() {
    RecipeComposeAppTheme {
        RecipeDetailsScreen(
            recipeId = RecipesRepositoryStub.getRecipeById(0)?.id ?: 0
        )
    }
}