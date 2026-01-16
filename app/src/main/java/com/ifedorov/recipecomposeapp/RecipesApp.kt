package com.ifedorov.recipecomposeapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7
import androidx.compose.ui.tooling.preview.Preview
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun RecipesApp() {
    RecipeComposeAppTheme {
        Scaffold { paddingValues ->
            Text(
                text = "Recipes App",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = PIXEL_7)
@Composable
private fun PreviewRecipesApp() {
    RecipesApp()
}