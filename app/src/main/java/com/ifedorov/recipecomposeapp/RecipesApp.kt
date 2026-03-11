package com.ifedorov.recipecomposeapp

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ifedorov.recipecomposeapp.core.datastore.FavoriteDataStoreManager
import com.ifedorov.recipecomposeapp.core.network.NetworkConfig.BASE_URL
import com.ifedorov.recipecomposeapp.core.network.api.RecipesApiService
import com.ifedorov.recipecomposeapp.core.ui.navigation.BottomNavigation
import com.ifedorov.recipecomposeapp.core.ui.navigation.Destination
import com.ifedorov.recipecomposeapp.core.utils.Constants.DEEP_LINK_SCHEME
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_ID
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_IMAGE_URL
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_TITLE
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_RECIPE_ID
import com.ifedorov.recipecomposeapp.data.database.RecipesDatabase
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepositoryImpl
import com.ifedorov.recipecomposeapp.features.categories.ui.CategoriesScreen
import com.ifedorov.recipecomposeapp.features.details.presentation.RecipeDetailsViewModel
import com.ifedorov.recipecomposeapp.features.details.ui.RecipeDetailsScreen
import com.ifedorov.recipecomposeapp.features.favorites.presentation.FavoritesViewModel
import com.ifedorov.recipecomposeapp.features.favorites.ui.FavoritesScreen
import com.ifedorov.recipecomposeapp.features.recipes.presentation.RecipesViewModel
import com.ifedorov.recipecomposeapp.features.recipes.ui.RecipesScreen
import com.ifedorov.recipecomposeapp.ui.theme.RecipeComposeAppTheme
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

@Composable
fun RecipesApp(
    deepLinkIntent: Intent?
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = remember(context) {
        context.applicationContext as Application
    }

    val favoriteDataStoreManager = remember(context) { FavoriteDataStoreManager(context) }
    val favoritesCount by favoriteDataStoreManager.getFavoriteCountFlow()
        .collectAsState(initial = 0)

    val json: Json = remember {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
    }

    val logging = remember {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    val okHttpClient: OkHttpClient = remember {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    val api: RecipesApiService = remember {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RecipesApiService::class.java)
    }

    val database = remember(context) {
        RecipesDatabase.buildDatabase(context)
    }

    val repository: RecipesRepository = remember(api, database) {
        RecipesRepositoryImpl(api, database)
    }

    LaunchedEffect(deepLinkIntent) {
        deepLinkIntent?.data?.let { uri ->
            val recipeId: Int? = when (uri.scheme) {
                DEEP_LINK_SCHEME -> {
                    if (uri.host == "recipe") uri.pathSegments[0].toIntOrNull() else null
                }

                "https", "http" -> {
                    if (uri.pathSegments[0] == "recipe") uri.pathSegments[1].toIntOrNull() else null
                }

                else -> null
            }

            if (recipeId != null) {
                delay(100)
                navController.navigate(Destination.RecipeDetails.createRoute(recipeId)) {
                    launchSingleTop = true
                }
            }
        }
    }

    RecipeComposeAppTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = {
                        val popped = navController.popBackStack(
                            route = Destination.Categories.route,
                            inclusive = false
                        )

                        if (!popped) {
                            navController.navigate(Destination.Categories.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    onFavoriteClick = {
                        navController.navigate(Destination.Favorites.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    favoriteCount = favoritesCount
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Destination.Categories.route
                ) {
                    composable(Destination.Categories.route) {
                        CategoriesScreen(
                            repository = repository,
                            onCategoryClick = { id, title, imageUrl ->
                                navController.navigate(
                                    Destination.Recipes.createRoute(
                                        id, title, imageUrl
                                    )
                                )
                            }
                        )
                    }
                    composable(Destination.Favorites.route) {
                        val viewModel: FavoritesViewModel = remember {
                            FavoritesViewModel(
                                application = application,
                                repository = repository
                            )
                        }

                        FavoritesScreen(
                            viewModel = viewModel,
                            onFavoriteRecipeClick = { recipeId ->
                                navController.navigate(
                                    Destination.RecipeDetails.createRoute(recipeId)
                                )
                            }
                        )
                    }
                    composable(
                        route = Destination.Recipes.route,
                        arguments = listOf(
                            navArgument(PARAM_CATEGORY_ID) { type = NavType.IntType },
                            navArgument(PARAM_CATEGORY_TITLE) { type = NavType.StringType },
                            navArgument(PARAM_CATEGORY_IMAGE_URL) { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val viewModel: RecipesViewModel = remember {
                            RecipesViewModel(
                                savedStateHandle = backStackEntry.savedStateHandle,
                                repository = repository
                            )
                        }

                        RecipesScreen(
                            viewModel = viewModel,
                            onRecipeClick = { recipeId ->
                                navController.navigate(
                                    Destination.RecipeDetails.createRoute(recipeId)
                                )
                            },
                        )
                    }
                    composable(
                        route = Destination.RecipeDetails.route,
                        arguments = listOf(navArgument(PARAM_RECIPE_ID) { type = NavType.IntType })
                    ) { backStackEntry ->

                        val viewModel: RecipeDetailsViewModel = remember {
                            RecipeDetailsViewModel(
                                savedStateHandle = backStackEntry.savedStateHandle,
                                repository = repository,
                                application = application
                            )
                        }

                        RecipeDetailsScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, device = PIXEL_7)
@Composable
private fun PreviewRecipesApp() {
    RecipesApp(
        deepLinkIntent = null
    )
}