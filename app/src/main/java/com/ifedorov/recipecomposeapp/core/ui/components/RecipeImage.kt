package com.ifedorov.recipecomposeapp.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.ifedorov.recipecomposeapp.R

@Composable
fun RecipeImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = remember(context, imageUrl) {
                ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(300)
                    .build()
            },
            contentDescription = contentDescription,
            contentScale = contentScale,
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_error),
            onLoading = { isLoading = true },
            onSuccess = { isLoading = false },
            onError = { isLoading = false },
            modifier = Modifier.fillMaxWidth()
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}