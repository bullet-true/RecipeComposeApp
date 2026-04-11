package com.ifedorov.recipecomposeapp.features.categories.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.ui.TestTags
import com.ifedorov.recipecomposeapp.core.ui.components.ScreenHeader
import com.ifedorov.recipecomposeapp.features.categories.presentation.CategoriesViewModel
import com.ifedorov.recipecomposeapp.features.categories.presentation.model.CategoriesUiState
import com.ifedorov.recipecomposeapp.features.categories.presentation.model.CategoryUiModel
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    viewModel: CategoriesViewModel = hiltViewModel(),
    onCategoryClick: (Int, String, String) -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    CategoriesContent(
        uiState = uiState,
        modifier = modifier,
        onCategoryClick = onCategoryClick
    )
}

@Composable
fun CategoriesContent(
    uiState: CategoriesUiState,
    modifier: Modifier = Modifier,
    onCategoryClick: (Int, String, String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            title = stringResource(R.string.categories),
            backgroundImage = painterResource(R.drawable.bcg_categories)
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

            uiState.categories.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.downloading_error_or_list_is_empty),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = uiState.categories,
                        key = { it.id }
                    ) { item ->
                        CategoryItem(
                            category = item,
                            onClick = { onCategoryClick(item.id, item.title, item.imageUrl) }
                        )
                    }
                }
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
        CategoriesContent(
            uiState = CategoriesUiState(
                categories = listOf(
                    CategoryUiModel(
                        id = 0,
                        title = "Бургеры",
                        description = "Рецепты всех популярных видов бургеров",
                        imageUrl = "burger.jpg"
                    ),
                    CategoryUiModel(
                        id = 1,
                        title = "Десерты",
                        description = "Самые вкусные рецепты десертов специально для вас",
                        imageUrl = "dessert.jpg"
                    )
                )
            ),
            onCategoryClick = { _, _, _ -> }
        )
    }
}