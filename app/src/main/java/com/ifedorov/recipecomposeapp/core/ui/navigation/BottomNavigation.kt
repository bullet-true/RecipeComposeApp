package com.ifedorov.recipecomposeapp.core.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme

@Composable
fun BottomNavigation(
    onCategoriesClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationButton(
            text = stringResource(R.string.categories),
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            textColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .weight(1f),
            onClick = { onCategoriesClick() }
        )
        Spacer(modifier = Modifier.width(4.dp))
        NavigationButton(
            text = stringResource(R.string.favorites),
            backgroundColor = MaterialTheme.colorScheme.error,
            textColor = MaterialTheme.colorScheme.surface,
            icon = painterResource(R.drawable.ic_heart_empty),
            modifier = Modifier
                .weight(1f),
            onClick = { onFavoriteClick() }
        )
    }
}

@Composable
private fun NavigationButton(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color,
    textColor: Color,
    icon: Painter? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = text.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = textColor
            )

            if (icon != null) {
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = textColor
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun PreviewBottomNavigation() {
    RecipeComposeAppTheme {
        BottomNavigation(
            onCategoriesClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 162, heightDp = 36)
@Composable
private fun PreviewNavigationButtonCategory() {
    RecipeComposeAppTheme {
        NavigationButton(
            text = stringResource(R.string.categories),
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            textColor = MaterialTheme.colorScheme.surface,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 162, heightDp = 36)
@Composable
private fun PreviewNavigationButtonFavorites() {
    RecipeComposeAppTheme {
        NavigationButton(
            text = stringResource(R.string.favorites),
            backgroundColor = MaterialTheme.colorScheme.error,
            textColor = MaterialTheme.colorScheme.surface,
            icon = painterResource(R.drawable.ic_heart_empty),
            onClick = {}
        )
    }
}