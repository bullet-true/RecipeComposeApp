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
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

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

        threadPool.execute {
            Log.i("!!!", "Категории: Выполняю запрос на потоке: ${Thread.currentThread().name}")

            var categoriesConnection: HttpURLConnection? = null
            val json = Json { ignoreUnknownKeys = true }

            try {
                val url = URL("https://recipes.androidsprint.ru/api/category")
                categoriesConnection = url.openConnection() as? HttpURLConnection

                val jsonBody = categoriesConnection?.getInputStream()?.let { inputStream ->
                    inputStream.use { stream ->
                        stream.bufferedReader().use { reader ->
                            reader.readText()
                        }
                    }
                }

                Log.i("!!!", "Response code: ${categoriesConnection?.responseCode}")
                Log.i("!!!", "Response message: ${categoriesConnection?.responseMessage}")
                Log.i("!!!", "Body: $jsonBody")

                if (jsonBody != null) {
                    val categories = json.decodeFromString<List<CategoryDto>>(jsonBody)
                    Log.i("!!!", "Category count: ${categories.size}")

                    categories.forEach { category ->
                        Log.i("!!!", category.title)

                        threadPool.execute {
                            var recipesConnection: HttpURLConnection? = null

                            try {
                                val url = URL("https://recipes.androidsprint.ru/api/category/${category.id}/recipes")

                                recipesConnection = url.openConnection() as? HttpURLConnection
                                recipesConnection?.connect()

                                val recipesJson =
                                    recipesConnection?.getInputStream()?.let { inputStream ->
                                        inputStream.use { stream ->
                                            stream.bufferedReader().use { reader ->
                                                reader.readText()
                                            }
                                        }
                                    }

                                if (recipesJson != null) {
                                    val recipes = json.decodeFromString<List<RecipeDto>>(recipesJson)

                                    Log.i(
                                        "Pool",
                                        "Рецепты на потоке ${Thread.currentThread().name}: ${category.title} - ${recipes.size} рецептов"
                                    )
                                }

                            } catch (e: Exception) {
                                Log.i("!!!", "Recipes connection error: $e")

                            } finally {
                                recipesConnection?.disconnect()
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.i("!!!", "Connection error: $e")

            } finally {
                categoriesConnection?.disconnect()
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