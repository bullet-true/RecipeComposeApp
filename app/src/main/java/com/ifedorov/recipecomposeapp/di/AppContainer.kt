package com.ifedorov.recipecomposeapp.di

import android.content.Context
import com.ifedorov.recipecomposeapp.BuildConfig
import com.ifedorov.recipecomposeapp.core.network.NetworkConfig.BASE_URL
import com.ifedorov.recipecomposeapp.core.network.api.RecipesApiService
import com.ifedorov.recipecomposeapp.data.database.RecipesDatabase
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryImpl
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

class AppContainer(context: Context) {
    private val json: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()


    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val recipesApi: RecipesApiService = retrofit.create(RecipesApiService::class.java)
    private val recipesDatabase: RecipesDatabase = RecipesDatabase.buildDatabase(context)

    val recipesRepository: RecipesRepository = RecipesRepositoryImpl(recipesApi, recipesDatabase)
}