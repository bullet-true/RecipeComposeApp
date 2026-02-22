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
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : ComponentActivity() {
    private var deepLinkIntent by mutableStateOf<Intent?>(null)

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

        val thread = Thread {
            Log.i("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

            var connection: HttpURLConnection? = null
            val json = Json { ignoreUnknownKeys = true }

            try {
                val url = URL("https://recipes.androidsprint.ru/api/category")
                connection = url.openConnection() as? HttpURLConnection

                val jsonBody = connection?.getInputStream()?.let { inputStream ->
                    inputStream.use { stream ->
                        stream.bufferedReader().use { reader ->
                            reader.readText()
                        }
                    }
                }

                Log.i("!!!", "Response code: ${connection?.responseCode}")
                Log.i("!!!", "Response message: ${connection?.responseMessage}")
                Log.i("!!!", "Body: $jsonBody")

                if (jsonBody != null) {
                    val categories = json.decodeFromString<List<CategoryDto>>(jsonBody)
                    Log.i("!!!", "Category count: ${categories.size}")

                    categories.forEach { category ->
                        Log.i("!!!", category.title)
                    }
                }

            } catch (e: Exception) {
                Log.i("!!!", "Connection error: $e")

            } finally {
                connection?.disconnect()
            }
        }

        thread.start()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        intent.data?.let {
            deepLinkIntent = intent
        }
        setIntent(intent)
    }
}