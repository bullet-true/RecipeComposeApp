package com.ifedorov.recipecomposeapp.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.ifedorov.recipecomposeapp.R

val montserratFontFamily = FontFamily(
    Font(
        resId = R.font.montserrat_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.montserrat_medium,
        weight = FontWeight.Medium
    )
)

val montserratAlternatesFontFamily = FontFamily(
    Font(
        resId = R.font.montserrat_alternates_semibold,
        weight = FontWeight.SemiBold
    )
)