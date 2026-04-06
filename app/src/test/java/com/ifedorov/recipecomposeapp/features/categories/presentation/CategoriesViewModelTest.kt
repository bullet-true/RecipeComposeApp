package com.ifedorov.recipecomposeapp.features.categories.presentation

import com.ifedorov.recipecomposeapp.core.utils.Constants.IMAGES_BASE_URL
import com.ifedorov.recipecomposeapp.data.repository.RecipesRepository
import fixtures.CategoryTestFixtures.createCategoryDtoList
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

class CategoriesViewModelTest {

    private val repository = mockk<RecipesRepository>()
    private lateinit var viewModel: CategoriesViewModel

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
    fun `loads categories from repository`() {
        val categories = createCategoryDtoList(count = TEST_LIST_SIZE)

        every { repository.getCategories() } returns flowOf(categories)

        viewModel = CategoriesViewModel(repository)
        val state = viewModel.uiState.value

        assertEquals(TEST_LIST_SIZE, state.categories.size)
        assertFalse(state.isLoading)
        assertEquals(TEST_ID, state.categories.first().id)
        assertEquals(TEST_TITLE, state.categories.first().title)
        assertEquals(TEST_DESCRIPTION, state.categories.first().description)
        assertEquals(IMAGES_BASE_URL + TEST_IMAGE_NAME, state.categories.first().imageUrl)
        assertEquals(null, state.error)
    }

    @Test
    fun `shows empty list when repository returns no data`() {
        every { repository.getCategories() } returns flowOf(emptyList())

        viewModel = CategoriesViewModel(repository)
        val state = viewModel.uiState.value

        assertTrue(state.categories.isEmpty())
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
    }

    @Test
    fun `shows error when repository throws`() {
        every { repository.getCategories() } returns flow { throw IOException(ERROR_MESSAGE) }

        viewModel = CategoriesViewModel(repository)
        val state = viewModel.uiState.value

        assertTrue(state.categories.isEmpty())
        assertFalse(state.isLoading)
        assertNotNull(state.error)
        assertEquals(ERROR_MESSAGE, state.error)
    }

    companion object {
        private const val TEST_LIST_SIZE = 2
        private const val TEST_ID = 1
        private const val TEST_TITLE = "Категория 1"
        private const val TEST_DESCRIPTION = "Рецепты всех популярных видов бургеров"
        private const val TEST_IMAGE_NAME = "burgers.png"
        private const val ERROR_MESSAGE = "Network error"
    }
}