package com.ifedorov.recipecomposeapp.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val DATASTORE_NAME = "recipe_app_prefs"
private const val SHARED_PREFS_NAME = "com.ifedorov.recipecomposeapp.FAVORITES_PREFS"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = DATASTORE_NAME,
    produceMigrations = { context ->
        listOf(
            SharedPreferencesMigration(
                context = context,
                sharedPreferencesName = SHARED_PREFS_NAME
            )
        )
    }
)