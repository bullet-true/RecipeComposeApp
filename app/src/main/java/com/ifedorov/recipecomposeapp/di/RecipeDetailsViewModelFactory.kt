package com.ifedorov.recipecomposeapp.di

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import com.ifedorov.recipecomposeapp.features.details.presentation.RecipeDetailsViewModel

class RecipeDetailsViewModelFactory(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val repository: RecipesRepository
) : Factory<RecipeDetailsViewModel> {

    override fun create(): RecipeDetailsViewModel {
        return RecipeDetailsViewModel(
            application = application,
            savedStateHandle = savedStateHandle,
            repository = repository
        )
    }
}