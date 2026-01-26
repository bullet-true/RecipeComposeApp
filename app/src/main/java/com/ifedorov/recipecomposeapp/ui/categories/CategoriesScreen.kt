package com.ifedorov.recipecomposeapp.ui.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.ui.components.ScreenHeader
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.ui.categories.model.toUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (Int, String) -> Unit
) {
    val categories = RecipesRepositoryStub.getCategories().map { it.toUiModel() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            title = stringResource(R.string.categories),
            backgroundImage = R.drawable.bcg_categories
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = categories,
                key = { it.id }
            ) { item ->
                CategoryItem(
                    category = item,
                    onClick = { onCategoryClick(item.id, item.title) }
                )
            }
        }
    }
}

@Preview(
    name = "Portrait",
    widthDp = 400,
    heightDp = 800
)
@Preview(
    name = "Landscape",
    widthDp = 800,
    heightDp = 400
)
@Composable
private fun PreviewCategoriesScreen() {
    RecipeComposeAppTheme {
        CategoriesScreen(
            onCategoryClick = { _, _ -> }
        )
    }
}