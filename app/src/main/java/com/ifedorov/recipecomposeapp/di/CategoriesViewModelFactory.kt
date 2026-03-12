package com.ifedorov.recipecomposeapp.di

import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import com.ifedorov.recipecomposeapp.features.categories.presentation.CategoriesViewModel

class CategoriesViewModelFactory(
    private val repository: RecipesRepository
) : Factory<CategoriesViewModel> {

    override fun create(): CategoriesViewModel {
        return CategoriesViewModel(repository)
    }
}