package com.ifedorov.recipecomposeapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    @ColumnInfo(name = "category_id") val categoryId: Int,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    val ingredients: List<String>,
    val method: List<String>,
)