package com.ifedorov.recipecomposeapp.di

import androidx.lifecycle.SavedStateHandle
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import com.ifedorov.recipecomposeapp.features.recipes.presentation.RecipesViewModel

class RecipesViewModelFactory(
    private val savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository
): Factory<RecipesViewModel> {

    override fun create(): RecipesViewModel {
        return RecipesViewModel(
            savedStateHandle = savedStateHandle,
            repository = repository
        )
    }
}