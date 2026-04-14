package com.ifedorov.recipecomposeapp.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesDataStoreTest {

    private lateinit var context: Context
    private lateinit var manager: FavoriteDataStoreManager

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        manager = FavoriteDataStoreManager(context)
    }

    @After
    fun tearDown() {
        runBlocking {
            context.dataStore.edit {
                it.clear()
            }
        }
    }

    @Test
    fun addFavoriteSavesRecipeId() = runTest {
        manager.addFavorite(TEST_RECIPE_ID)

        val favorites = manager.getFavoriteIdsFlow().first()
        assertTrue(favorites.contains(TEST_RECIPE_ID.toString()))
    }

    @Test
    fun removeFromFavoritesDeletesRecipeId() = runTest {
        manager.addFavorite(TEST_RECIPE_ID)
        manager.removeFavorite(TEST_RECIPE_ID)

        val favorites = manager.getFavoriteIdsFlow().first()
        assertFalse(favorites.contains(TEST_RECIPE_ID.toString()))
    }

    @Test
    fun favoritesFlowEmitsUpdatesReactively() = runTest {
        manager.getFavoriteIdsFlow().test {
            val initial = awaitItem()
            assertTrue(initial.isEmpty())

            manager.addFavorite(TEST_RECIPE_ID)

            val updated = awaitItem()
            assertTrue(updated.contains(TEST_RECIPE_ID.toString()))

            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {
        private const val TEST_RECIPE_ID = 42
    }
}