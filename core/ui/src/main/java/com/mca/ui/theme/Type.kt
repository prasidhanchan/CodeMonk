package com.mca.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mca.ui.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

val dosis = FontFamily(
    Font(R.font.dosis_regular, weight = FontWeight.Normal),
    Font(R.font.dosis_bold, weight = FontWeight.Bold),
    Font(R.font.dosis_semibold, weight = FontWeight.SemiBold),
    Font(R.font.dosis_extrabold, weight = FontWeight.ExtraBold),
    Font(R.font.dosis_light, weight = FontWeight.Light),
    Font(R.font.dosis_extralight, weight = FontWeight.ExtraLight),
    Font(R.font.dosis_medium, weight = FontWeight.Medium)
)