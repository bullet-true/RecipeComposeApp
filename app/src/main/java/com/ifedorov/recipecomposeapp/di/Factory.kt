package com.ifedorov.recipecomposeapp.di

interface Factory<T> {
    fun create(): T
}