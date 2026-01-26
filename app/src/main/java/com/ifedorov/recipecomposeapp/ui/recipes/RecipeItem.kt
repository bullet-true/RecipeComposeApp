package com.ifedorov.recipecomposeapp.ui.recipes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.ui.recipes.model.RecipeUiModel
import com.ifedorov.recipecomposeapp.ui.recipes.model.toUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import java.util.Locale

@Composable
fun RecipeItem(
    recipe: RecipeUiModel,
    onRecipeClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onRecipeClick(recipe.id) },
        modifier = modifier.height(132.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Text(
                text = recipe.title.uppercase(Locale.getDefault()),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(widthDp = 328)
@Composable
fun PreviewRecipeItem() {
    RecipeComposeAppTheme {
        RecipeItem(
            recipe = RecipesRepositoryStub.getRecipesByCategoryId(0).first().toUiModel(),
            onRecipeClick = {}
        )
    }
}