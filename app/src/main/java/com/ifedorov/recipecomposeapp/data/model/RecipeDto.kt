package com.ifedorov.recipecomposeapp.data.model

import com.ifedorov.recipecomposeapp.data.database.entity.RecipeEntity
import kotlinx.serialization.Serializable

private const val INGREDIENT_PARTS_SEPARATOR = "###"
private const val EMPTY_STRING = ""

@Serializable
data class RecipeDto(
    val id: Int,
    val title: String,
    val ingredients: List<IngredientDto>,
    val method: List<String>,
    val imageUrl: String,
    val categoryIds: List<Int> = emptyList(),
)

fun RecipeDto.toEntity(categoryId: Int) = RecipeEntity(
    id = id,
    title = title,
    categoryId = categoryId,
    imageUrl = imageUrl,
    ingredients = ingredients.map { ingredientDto ->
        listOf(
            ingredientDto.quantity,
            ingredientDto.unitOfMeasure,
            ingredientDto.description
        ).joinToString(INGREDIENT_PARTS_SEPARATOR)
    },
    method = method
)

fun RecipeEntity.toDto() = RecipeDto(
    id = id,
    title = title,
    ingredients =
        ingredients.map { ingredientString ->
            val parts = ingredientString.split(INGREDIENT_PARTS_SEPARATOR)

            IngredientDto(
                quantity = parts.getOrElse(0) { EMPTY_STRING },
                unitOfMeasure = parts.getOrElse(1) { EMPTY_STRING },
                description = parts.getOrElse(2) { EMPTY_STRING }
            )
        },
    method = method,
    imageUrl = imageUrl,
    categoryIds = listOf(categoryId)
)