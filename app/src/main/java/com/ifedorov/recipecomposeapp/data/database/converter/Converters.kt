package com.ifedorov.recipecomposeapp.data.database.converter

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromString(value: String): List<String> =
        if (value.isBlank()) emptyList() else value.split(SEPARATOR)

    @TypeConverter
    fun fromList(list: List<String>): String = list.joinToString(SEPARATOR)

    companion object {
        private const val SEPARATOR = "|||"
    }
}