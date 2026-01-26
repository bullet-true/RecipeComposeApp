package com.ifedorov.recipecomposeapp.ui.categories

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.ui.categories.model.CategoryUiModel
import com.ifedorov.recipecomposeapp.ui.categories.model.toUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import java.util.Locale

@Composable
fun CategoryItem(
    category: CategoryUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(220.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)

    ) {
        Column {
            AsyncImage(
                model = category.imageUrl,
                contentDescription = category.title,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = category.title.uppercase(Locale.getDefault()),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = category.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(widthDp = 160)
@Composable
fun PreviewCategoryItem() {
    RecipeComposeAppTheme {
        CategoryItem(
            category = RecipesRepositoryStub.getCategories().first().toUiModel(),
            onClick = {}
        )
    }
}