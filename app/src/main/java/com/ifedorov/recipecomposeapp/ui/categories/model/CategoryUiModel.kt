package com.ifedorov.recipecomposeapp.ui.categories.model

import androidx.compose.runtime.Immutable
import com.ifedorov.recipecomposeapp.core.Constants
import com.ifedorov.recipecomposeapp.data.model.CategoryDto

@Immutable
data class CategoryUiModel(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
)

fun CategoryDto.toUiModel() = CategoryUiModel(
    id = id,
    title = title,
    description = description,
    imageUrl = if (imageUrl.startsWith(prefix = "http", ignoreCase = true)) {
        imageUrl
    } else {
        Constants.ASSETS_URI_PREFIX + imageUrl
    }
)