package com.ifedorov.recipecomposeapp.features.categories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import com.ifedorov.recipecomposeapp.features.categories.presentation.model.CategoriesUiState
import com.ifedorov.recipecomposeapp.features.categories.presentation.model.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel(private val repository: RecipesRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoriesUiState())
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val categories = repository.getCategories().map { it.toUiModel() }

                _uiState.update { currentState ->
                    currentState.copy(
                        categories = categories,
                        isLoading = false
                    )
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
}