package com.ifedorov.recipecomposeapp.features.details.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ifedorov.recipecomposeapp.core.datastore.FavoriteDataStoreManager
import com.ifedorov.recipecomposeapp.core.extensions.IngredientExtensions.scaled
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_RECIPE_ID
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import com.ifedorov.recipecomposeapp.features.details.presentation.model.RecipeDetailsUiState
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.IngredientUiModel
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.RecipeUiModel
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository,
) : AndroidViewModel(application) {

    private val favoriteDataStoreManager = FavoriteDataStoreManager(application)

    private val recipeId: Int = savedStateHandle.get<Int>(PARAM_RECIPE_ID) ?: 0

    private val _uiState = MutableStateFlow(RecipeDetailsUiState(isLoading = true))
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState
        .combine(favoriteDataStoreManager.isFavoriteFlow(recipeId)) { currentState, isFavorite ->
            currentState.copy(isFavorite = isFavorite)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _uiState.value
        )

    init {
        viewModelScope.launch {
            try {
                repository.getRecipe(recipeId).collect { recipeDto ->
                    if (recipeDto == null) {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = true,
                                recipe = null,
                                scaledIngredients = emptyList(),
                                error = null
                            )
                        }

                    } else {
                        val recipe = recipeDto.toUiModel()
                        val scaledIngredients =
                            recipe.scaleIngredients(_uiState.value.currentPortions)

                        _uiState.update { currentState ->
                            currentState.copy(
                                recipe = recipe,
                                scaledIngredients = scaledIngredients,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
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

    fun toggleFavorite() {
        viewModelScope.launch {
            if (uiState.value.isFavorite) {
                favoriteDataStoreManager.removeFavorite(recipeId)
            } else {
                favoriteDataStoreManager.addFavorite(recipeId)
            }
        }
    }

    fun updatePortions(newValue: Int) {
        _uiState.update { currentState ->
            val scaledIngredients = currentState.recipe?.scaleIngredients(newValue).orEmpty()

            currentState.copy(
                currentPortions = newValue,
                scaledIngredients = scaledIngredients
            )
        }
    }

    private fun RecipeUiModel.scaleIngredients(portions: Int): List<IngredientUiModel> {
        val multiplier = portions.toDouble() / servings.coerceAtLeast(1)
        return ingredients.map { it.scaled(multiplier) }
    }
}