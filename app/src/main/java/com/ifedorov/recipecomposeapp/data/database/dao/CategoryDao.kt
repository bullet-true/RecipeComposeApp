package com.ifedorov.recipecomposeapp.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.ifedorov.recipecomposeapp.data.database.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name")
    fun getCategories(): Flow<List<CategoryEntity>>
}