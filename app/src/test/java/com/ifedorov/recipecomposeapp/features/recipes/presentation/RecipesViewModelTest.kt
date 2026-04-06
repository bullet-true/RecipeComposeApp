package com.ifedorov.recipecomposeapp.features.recipes.presentation

import androidx.lifecycle.SavedStateHandle
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_ID
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_IMAGE_URL
import com.ifedorov.recipecomposeapp.core.utils.Constants.PARAM_CATEGORY_TITLE
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import fixtures.RecipeTestFixtures.createRecipeDtoList
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RecipesViewModelTest {

    private val repository = mockk<RecipesRepository>()
    private lateinit var viewModel: RecipesViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `loads recipes for category`() {
        val recipes = createRecipeDtoList(TEST_LIST_SIZE)

        every { repository.getRecipesByCategory(TEST_CATEGORY_ID) } returns flowOf(recipes)

        viewModel = createViewModel()
        val state = viewModel.uiState.value

        assertEquals(TEST_LIST_SIZE, state.recipes.size)
        assertEquals(TEST_RECIPE_ID_FIRST, state.recipes.first().id)
        assertEquals(TEST_RECIPE_TITLE_FIRST, state.recipes.first().title)
        assertEquals(TEST_RECIPE_ID_SECOND, state.recipes.last().id)
        assertEquals(TEST_RECIPE_TITLE_SECOND, state.recipes.last().title)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `state reflects category title from savedState`() {
        every { repository.getRecipesByCategory(TEST_CATEGORY_ID) } returns flowOf(emptyList())

        viewModel = createViewModel(categoryTitle = TEST_CATEGORY_TITLE)
        val state = viewModel.uiState.value

        assertEquals(TEST_CATEGORY_TITLE, state.categoryTitle)
        assertEquals(TEST_CATEGORY_ID, state.categoryId)
        assertEquals(TEST_CATEGORY_IMAGE_URL, state.categoryImageUrl)
        assertTrue(state.recipes.isEmpty())
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `shows error when repository throws`() {
        every { repository.getRecipesByCategory(TEST_CATEGORY_ID) } returns flow {
            throw IOException(ERROR_MESSAGE)
        }

        viewModel = createViewModel()
        val state = viewModel.uiState.value

        assertTrue(state.recipes.isEmpty())
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertEquals(ERROR_MESSAGE, state.error)

        assertEquals(TEST_CATEGORY_ID, state.categoryId)
        assertEquals(TEST_CATEGORY_TITLE, state.categoryTitle)
        assertEquals(TEST_CATEGORY_IMAGE_URL, state.categoryImageUrl)
    }

    private fun createViewModel(
        categoryId: Int = TEST_CATEGORY_ID,
        categoryTitle: String = TEST_CATEGORY_TITLE,
        categoryImageUrl: String = TEST_CATEGORY_IMAGE_URL
    ) = RecipesViewModel(
        savedStateHandle = SavedStateHandle(
            mapOf(
                PARAM_CATEGORY_ID to categoryId,
                PARAM_CATEGORY_TITLE to categoryTitle,
                PARAM_CATEGORY_IMAGE_URL to categoryImageUrl
            )
        ),
        repository = repository
    )

    companion object {
        private const val TEST_CATEGORY_ID = 1
        private const val TEST_CATEGORY_TITLE = "Завтраки"
        private const val TEST_CATEGORY_IMAGE_URL = "breakfast.jpg"
        private const val ERROR_MESSAGE = "Network error"
        private const val TEST_LIST_SIZE = 2

        private const val TEST_RECIPE_ID_FIRST = 1
        private const val TEST_RECIPE_ID_SECOND = 2
        private const val TEST_RECIPE_TITLE_FIRST = "Рецепт 1"
        private const val TEST_RECIPE_TITLE_SECOND = "Рецепт 2"
    }
}