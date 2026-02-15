package com.ifedorov.recipecomposeapp.core.sharedPrefs

import android.content.Context
import androidx.core.content.edit
import com.ifedorov.recipecomposeapp.R

class FavoritePrefsManager(context: Context) {
    private val prefsName = context.getString(R.string.preference_favorites_recipes)
    private val favoritesKey = context.getString(R.string.favorite_recipe_ids)
    private val sharedPrefs = context.getSharedPreferences(
        prefsName, Context.MODE_PRIVATE
    )

    fun isFavorite(recipeId: Int): Boolean {
        val favoriteRecipesIds = getAllFavorites()
        return favoriteRecipesIds.contains(recipeId.toString())
    }

    fun addToFavorites(recipeId: Int) {
        val favoriteRecipesIds = getAllFavorites()
        val updatedFavoriteRecipesIds = favoriteRecipesIds.toMutableSet()

        updatedFavoriteRecipesIds.add(recipeId.toString())
        sharedPrefs.edit {
            putStringSet(favoritesKey, updatedFavoriteRecipesIds)
        }
    }

    fun removeFromFavorites(recipeId: Int) {
        val favoriteRecipesIds = getAllFavorites()
        val updatedFavoriteRecipesIds = favoriteRecipesIds.toMutableSet()

        updatedFavoriteRecipesIds.remove(recipeId.toString())
        sharedPrefs.edit {
            putStringSet(favoritesKey, updatedFavoriteRecipesIds)
        }
    }

    fun getAllFavorites(): Set<String> = sharedPrefs.getStringSet(favoritesKey, emptySet()).orEmpty()
}