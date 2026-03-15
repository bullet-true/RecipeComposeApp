package com.ifedorov.recipecomposeapp.features.favorites.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ifedorov.recipecomposeapp.core.datastore.FavoriteDataStoreManager
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import com.ifedorov.recipecomposeapp.features.favorites.presentation.model.FavoritesUiState
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    application: Application,
    private val repository: RecipesRepository,
) : AndroidViewModel(application) {

    private val favoriteDataStoreManager = FavoriteDataStoreManager(application)

    val uiState: StateFlow<FavoritesUiState> = favoriteDataStoreManager
        .getFavoriteIdsFlow()
        .map { favoriteIds ->
            favoriteIds.mapNotNull { idString -> idString.toIntOrNull() }
        }
        .flatMapLatest { favoriteIds ->
            if (favoriteIds.isEmpty()) {
                flowOf(
                    FavoritesUiState(
                        favoriteRecipes = emptyList(),
                        isLoading = false,
                        error = null
                    )
                )
            } else {
                repository.getRecipesByIds(favoriteIds).map { recipes ->
                    val recipesById = recipes.associateBy { it.id }

                    FavoritesUiState(
                        favoriteRecipes = favoriteIds.mapNotNull { id ->
                            recipesById[id]?.toUiModel(isFavorite = true)
                        },
                        isLoading = false,
                        error = null
                    )
                }
            }
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