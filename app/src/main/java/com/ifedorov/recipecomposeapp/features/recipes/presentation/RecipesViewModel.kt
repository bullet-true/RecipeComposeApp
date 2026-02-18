package com.ifedorov.recipecomposeapp.features.recipes.presentation

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_ID
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_IMAGE_URL
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_TITLE
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryStub
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.RecipesUiState
import com.ifedorov.recipecomposeapp.features.recipes.presentation.model.toUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipesViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _uiState = MutableStateFlow(RecipesUiState())
    val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

    private val categoryId: Int = savedStateHandle.get<Int>(PARAM_CATEGORY_ID) ?: 0
    private val categoryTitle: String = savedStateHandle.get<String>(PARAM_CATEGORY_TITLE)
        ?.let { Uri.decode(it) }
        ?: ""

    private val categoryImageUrl: String = savedStateHandle.get<String>(PARAM_CATEGORY_IMAGE_URL)
        ?.let { Uri.decode(it) }
        ?: ""

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
                delay(500)
                val recipes =
                    RecipesRepositoryStub.getRecipesByCategoryId(categoryId).map { it.toUiModel() }

                _uiState.update { currentState ->
                    currentState.copy(
                        recipes = recipes,
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