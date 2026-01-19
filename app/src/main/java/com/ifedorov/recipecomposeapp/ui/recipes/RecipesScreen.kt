package com.ifedorov.recipecomposeapp.ui.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.ui.components.ScreenHeader
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun RecipesScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ScreenHeader(
            title = "Категория",
            backgroundImage = R.drawable.img_error
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Скоро здесь будет список рецептов",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRecipesScreen() {
    RecipeComposeAppTheme {
        RecipesScreen()
    }
}