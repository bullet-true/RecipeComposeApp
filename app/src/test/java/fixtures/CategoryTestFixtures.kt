package fixtures

import com.ifedorov.recipecomposeapp.data.model.CategoryDto

object CategoryTestFixtures {

    fun createCategoryDto(
        id: Int = TEST_ID,
        title: String = TEST_TITLE,
        description: String = TEST_DESCRIPTION,
        imageUrl: String = TEST_IMAGE_NAME
    ) = CategoryDto(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl
    )

    fun createCategoryDtoList(count: Int = TEST_LIST_SIZE): List<CategoryDto> =
        List(count) { index ->
            createCategoryDto(
                id = index + 1,
                title = "Категория ${index + 1}"
            )
        }

    const val TEST_ID = 0
    const val TEST_TITLE = "Бургеры"
    const val TEST_DESCRIPTION = "Рецепты всех популярных видов бургеров"
    const val TEST_IMAGE_NAME = "burgers.png"
    const val TEST_LIST_SIZE = 3
    const val EMPTY_TITLE = ""
    val TEST_LONG_DESCRIPTION = "Очень длинное описание ".repeat(20)
}