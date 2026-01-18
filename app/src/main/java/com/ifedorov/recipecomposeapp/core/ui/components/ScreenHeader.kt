package com.ifedorov.recipecomposeapp.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import java.util.Locale

@Composable
fun ScreenHeader(
    title: String,
    @DrawableRes backgroundImage: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(224.dp)
    ) {
        Image(
            painter = painterResource(id = backgroundImage),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = title.uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreenHeader() {
    RecipeComposeAppTheme {
        ScreenHeader(
            title = stringResource(R.string.categories),
            backgroundImage = R.drawable.img_error
        )
    }
}