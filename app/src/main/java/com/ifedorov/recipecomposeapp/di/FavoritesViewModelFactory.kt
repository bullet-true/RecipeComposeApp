package com.ifedorov.recipecomposeapp.di

import android.app.Application
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import com.ifedorov.recipecomposeapp.features.favorites.presentation.FavoritesViewModel

class FavoritesViewModelFactory(
    private val application: Application,
    private val repository: RecipesRepository
) : Factory<FavoritesViewModel> {

    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(
            application = application,
            repository = repository
        )
    }
}