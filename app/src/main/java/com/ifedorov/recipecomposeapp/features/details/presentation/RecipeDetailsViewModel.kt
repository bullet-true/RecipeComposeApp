package com.ifedorov.recipecomposeapp.features.details.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.ifedorov.recipecomposeapp.R
import com.ifedorov.recipecomposeapp.core.datastore.FavoriteDataStoreManager
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_RECIPE_ID
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.features.details.presentation.model.RecipeDetailsUiState
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val favoriteDataStoreManager = FavoriteDataStoreManager(application)

    private val _uiState = MutableStateFlow(RecipeDetailsUiState())
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    private val recipeId: Int = savedStateHandle.get<Int>(PARAM_RECIPE_ID) ?: 0

    init {
        loadRecipe()
        observeFavorite()
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                favoriteDataStoreManager.removeFavorite(recipeId)
            } else {
                favoriteDataStoreManager.addFavorite(recipeId)
            }
        }
    }

    fun updatePortions(newValue: Int) {
        _uiState.update { it.copy(currentPortions = newValue) }
    }

    private fun loadRecipe() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val recipe = RecipesRepositoryStub.getRecipeById(recipeId)?.toUiModel()
                if (recipe == null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            error = application.getString(R.string.recipe_not_found)
                        )
                    }
                } else {
                    _uiState.update { it.copy(recipe = recipe, isLoading = false) }
                }

            } catch (e: Exception) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    private fun observeFavorite() {
        viewModelScope.launch {
            favoriteDataStoreManager.isFavoriteFlow(recipeId)
                .catch { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
                .collect { isFavorite ->
                    _uiState.update { it.copy(isFavorite = isFavorite) }
                }
        }
    }
}