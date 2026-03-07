package com.ifedorov.recipecomposeapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
)