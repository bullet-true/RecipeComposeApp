package com.ifedorov.recipecomposeapp.features.categories.presentation.model

import androidx.compose.runtime.Immutable
import com.ifedorov.recipecomposeapp.core.utils.Constants
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
        Constants.IMAGES_BASE_URL + imageUrl
    }
)