package br.com.omie.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import br.com.omie.R

// Set of Material typography styles to start with

val openSansFamily = FontFamily(
    Font(R.font.opensans_light, FontWeight.Light),
    Font(R.font.opensans_regular, FontWeight.Normal),
    Font(R.font.opensans_semibold, FontWeight.SemiBold),
    Font(R.font.opensans_bold, FontWeight.Bold),
)

private val defaultTypography = Typography()
val Typography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = openSansFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = openSansFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = openSansFamily),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = openSansFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = openSansFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = openSansFamily),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = openSansFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = openSansFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = openSansFamily),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = openSansFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = openSansFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = openSansFamily),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = openSansFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = openSansFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = openSansFamily)
)