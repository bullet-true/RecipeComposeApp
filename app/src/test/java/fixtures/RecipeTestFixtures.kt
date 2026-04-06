package fixtures

import com.ifedorov.recipecomposeapp.data.model.IngredientDto
import com.ifedorov.recipecomposeapp.data.model.RecipeDto

object RecipeTestFixtures {

    fun createIngredientDto(
        quantity: String = TEST_QUANTITY,
        unitOfMeasure: String = TEST_UNIT_OF_MEASURE,
        description: String = TEST_DESCRIPTION,
    ) = IngredientDto(
        quantity = quantity,
        unitOfMeasure = unitOfMeasure,
        description = description
    )

    fun createRecipeDto(
        id: Int = TEST_ID,
        title: String = TEST_TITLE,
        ingredients: List<IngredientDto> = TEST_INGREDIENTS,
        method: List<String> = TEST_METHOD,
        imageUrl: String = TEST_IMAGE_NAME,
        categoryIds: List<Int> = TEST_CATEGORY_IDS
    ) = RecipeDto(
        id = id,
        title = title,
        ingredients = ingredients,
        method = method,
        imageUrl = imageUrl,
        categoryIds = categoryIds
    )

    fun createRecipeDtoList(count: Int = TEST_LIST_SIZE): List<RecipeDto> =
        List(count) { index ->
            createRecipeDto(
                id = index + 1,
                title = "Рецепт ${index + 1}"
            )
        }

    const val TEST_DESCRIPTION = "говяжий фарш"
    const val TEST_QUANTITY = "0.5"
    const val TEST_UNIT_OF_MEASURE = "кг"
    const val TEST_ID = 1
    const val TEST_TITLE = "Burger"
    const val TEST_IMAGE_URL = "https://images/burger.png"
    const val TEST_IMAGE_NAME = "burger.png"
    const val TEST_LIST_SIZE = 3
    const val TEST_SERVINGS = 1
    val TEST_INGREDIENTS = listOf(createIngredientDto())
    val TEST_METHOD = listOf("Пункт 1", "Пункт 2")
    val TEST_CATEGORY_IDS = listOf(0)
}