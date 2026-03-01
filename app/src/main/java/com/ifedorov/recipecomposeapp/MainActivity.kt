package com.ifedorov.recipecomposeapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.ifedorov.recipecomposeapp.core.network.NetworkConfig.BASE_URL
import com.ifedorov.recipecomposeapp.core.network.api.RecipesApiService
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val retrofitService = retrofit.create(RecipesApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        intent?.data?.let {
            deepLinkIntent = intent
        }

        setContent {
            RecipesApp(deepLinkIntent = deepLinkIntent)
        }

        lifecycleScope.launch {
            try {
                val categories = retrofitService.getCategories()

                Log.i("!!!", "Categories: $categories")
                Log.i("!!!", "Category count: ${categories.size}")

                categories.map { category ->
                    launch {
                        Log.i("!!!", "Категория: ${category.title}")

                        try {
                            val recipes = retrofitService.getRecipesByCategoryId(category.id)
                            Log.i("!!!", "Рецепт: ${category.title} - ${recipes.size} рецептов")

                        } catch (e: Exception) {
                            Log.e("!!!", "Ошибка загрузки рецептов для ${category.title}", e)
                        }
                    }
                }.joinAll()

            } catch (e: Exception) {
                Log.e("!!!", "Ошибка загрузки категорий", e)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent.data?.let {
            deepLinkIntent = intent
        }
        setIntent(intent)
    }
}