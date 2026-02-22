package com.ifedorov.recipecomposeapp.features.favorites.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ifedorov.recipecomposeapp.core.datastore.FavoriteDataStoreManager
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.features.favorites.presentation.model.FavoritesUiState
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteDataStoreManager = FavoriteDataStoreManager(application)

    val uiState: StateFlow<FavoritesUiState> = favoriteDataStoreManager
        .getFavoriteIdsFlow()
        .map { favoriteIds ->
            val recipes = favoriteIds.mapNotNull { idString ->
                val id = idString.toIntOrNull()

                id?.let {
                    RecipesRepositoryStub.getRecipeById(it)?.toUiModel(isFavorite = true)
                }
            }

            FavoritesUiState(
                favoriteRecipes = recipes,
                isLoading = false,
                error = null
            )

        }.onStart {
            emit(FavoritesUiState(isLoading = true))
        }.catch { e ->
            emit(
                FavoritesUiState(
                    favoriteRecipes = emptyList(),
                    isLoading = false,
                    error = e.message
                )
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            FavoritesUiState()
        )
}