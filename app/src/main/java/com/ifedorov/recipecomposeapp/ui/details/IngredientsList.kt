package com.ifedorov.recipecomposeapp.ui.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.ui.recipes.model.IngredientUiModel
import com.ifedorov.recipecomposeapp.ui.recipes.model.toUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun IngredientsList(
    ingredients: List<IngredientUiModel>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            ingredients.forEachIndexed { index, ingredient ->
                IngredientItem(
                    ingredient = ingredient,
                    modifier = Modifier.fillMaxWidth()
                )

                if (index != ingredients.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewIngredientsList() {
    RecipeComposeAppTheme {
        IngredientsList(
            RecipesRepositoryStub.getRecipesByCategoryId(0).first().toUiModel().ingredients
        )
    }
}