package com.ifedorov.recipecomposeapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.extensions.scaled
import com.ifedorov.recipecomposeapp.core.ui.components.ScreenHeader
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.ui.recipes.model.RecipeUiModel
import com.ifedorov.recipecomposeapp.ui.recipes.model.toUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import java.util.Locale

@Composable
fun RecipeDetailsScreen(
    recipe: RecipeUiModel,
    modifier: Modifier = Modifier
) {
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
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                PortionsSelector(
                    currentPortions = currentPortions,
                    onPortionsChange = { newValue ->
                        currentPortions = newValue
                    }
                )
            }
            item {
                IngredientsList(scaledIngredients)
            }
            item {
                Text(
                    text = stringResource(R.string.method).uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                InstructionsList(recipe.method)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipeDetailsScreen() {
    RecipeComposeAppTheme {
        RecipeDetailsScreen(
            recipe = RecipesRepositoryStub.getRecipesByCategoryId(0).first().toUiModel()
        )
    }
}