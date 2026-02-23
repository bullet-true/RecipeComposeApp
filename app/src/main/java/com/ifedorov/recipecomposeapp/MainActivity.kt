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
import com.ifedorov.recipecomposeapp.data.model.CategoryDto
import com.ifedorov.recipecomposeapp.data.model.RecipeDto
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)
    private val okHttpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        intent?.data?.let {
            deepLinkIntent = intent
        }

        setContent {
            RecipesApp(deepLinkIntent = deepLinkIntent)
        }

        Log.i("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")

        thread {
            Log.i("!!!", "Категории: Выполняю запрос на потоке: ${Thread.currentThread().name}")

            val json = Json { ignoreUnknownKeys = true }

            try {
                val request = Request.Builder()
                    .url("https://recipes.androidsprint.ru/api/category")
                    .build()

                val response = okHttpClient.newCall(request).execute()
                val jsonBody = response.body?.string()


                Log.i("!!!", "Response code: ${response.code}")
                Log.i("!!!", "Response message: ${response.message}")
                Log.i("!!!", "Body: $jsonBody")

                if (jsonBody != null) {
                    val categories = json.decodeFromString<List<CategoryDto>>(jsonBody)
                    Log.i("!!!", "Category count: ${categories.size}")

                    categories.forEach { category ->
                        Log.i("!!!", category.title)

                        threadPool.execute {
                            try {
                                val request = Request.Builder()
                                    .url("https://recipes.androidsprint.ru/api/category/${category.id}/recipes")
                                    .build()

                                val response = okHttpClient.newCall(request).execute()
                                val recipesJsonBody = response.body?.string()

                                if (recipesJsonBody != null) {
                                    val recipes =
                                        json.decodeFromString<List<RecipeDto>>(recipesJsonBody)

                                    Log.i(
                                        "Pool",
                                        "Рецепты на потоке ${Thread.currentThread().name}: ${category.title} - ${recipes.size} рецептов"
                                    )
                                }

                            } catch (e: Exception) {
                                Log.i("!!!", "Recipes connection error: $e")
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.i("!!!", "Connection error: $e")
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

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}