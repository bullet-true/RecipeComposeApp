package com.ifedorov.recipecomposeapp.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class FavoriteDataStoreManager(
    private val context: Context
) {
    fun getFavoriteIdsFlow(): Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.FAVORITE_RECIPE_IDS] ?: emptySet()
    }

    fun isFavoriteFlow(recipeId: Int): Flow<Boolean> = getFavoriteIdsFlow().map { favoritesIds ->
        favoritesIds.contains(recipeId.toString())
    }

    fun getFavoriteCountFlow(): Flow<Int> = getFavoriteIdsFlow().map { it.size }

    suspend fun isFavorite(recipeId: Int): Boolean {
        val preferences = context.dataStore.data.first()
        val favoritesIds = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] ?: emptySet()
        return favoritesIds.contains(recipeId.toString())
    }

    suspend fun addFavorite(recipeId: Int) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] ?: emptySet()
            val updatedFavorites = currentFavorites + recipeId.toString()
            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] = updatedFavorites
        }
    }

    suspend fun removeFavorite(recipeId: Int) {
        context.dataStore.edit { preferences ->
            val currentFavorites = preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] ?: emptySet()
            val updatedFavorites = currentFavorites - recipeId.toString()
            preferences[PreferencesKeys.FAVORITE_RECIPE_IDS] = updatedFavorites
        }
    }
}