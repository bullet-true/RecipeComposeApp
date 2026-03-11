package com.ifedorov.recipecomposeapp.features.recipes.presentation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_ID
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_IMAGE_URL
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_TITLE
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.RecipesUiState
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipesViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipesUiState())
    val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

    private val categoryId: Int = savedStateHandle.get<Int>(PARAM_CATEGORY_ID) ?: 0
    private val categoryTitle: String = savedStateHandle.get<String>(PARAM_CATEGORY_TITLE)
        ?.let { Uri.decode(it) }
        ?: EMPTY_STRING

    private val categoryImageUrl: String = savedStateHandle.get<String>(PARAM_CATEGORY_IMAGE_URL)
        ?.let { Uri.decode(it) }
        ?: EMPTY_STRING

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    categoryId = categoryId,
                    categoryTitle = categoryTitle,
                    categoryImageUrl = categoryImageUrl,
                    isLoading = true,
                    error = null
                )
            }

            try {
                repository.getRecipesByCategory(categoryId).collect { recipesDto ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            recipes = recipesDto.map { it.toUiModel() },
                            isLoading = false,
                            error = null
                        )
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

    companion object {
        private const val EMPTY_STRING = ""
    }
}