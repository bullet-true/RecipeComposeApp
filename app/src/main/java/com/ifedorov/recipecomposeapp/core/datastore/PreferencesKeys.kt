package com.ifedorov.recipecomposeapp.core.datastore

import androidx.datastore.preferences.core.stringSetPreferencesKey

object PreferencesKeys {
    val FAVORITE_RECIPE_IDS = stringSetPreferencesKey("favorite_recipe_ids")
}