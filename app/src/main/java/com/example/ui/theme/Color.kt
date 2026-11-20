package com.example.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val KavachTeal = Color(0xFF1F6F78)
val KavachInk = Color(0xFF123F46)
val TealTint = Color(0xFFCFE3E4)
val Mist = Color(0xFFF3F7F6)
val DawnSand = Color(0xFFEFE6D8)
val Sage = Color(0xFF6FAE95)
val SageTint = Color(0xFFDDEFE7)
val SageDeep = Color(0xFF1E5A47)
val Saffron = Color(0xFFE3A53A)
val SaffronTint = Color(0xFFFBEFD6)
val SaffronDeep = Color(0xFF6B4A0F)
val GuideViolet = Color(0xFF6F66B8)
val VioletTint = Color(0xFFE4E1F4)
val VioletDeep = Color(0xFF3F3875)
val SosRed = Color(0xFFC73B3B)
val SosTint = Color(0xFFF8E1E1)
val InkSecondary = Color(0xFF4F7075)
val Hairline = Color(0xFFD5DEDD)
val CrimsonDark = Color(0xFF9E2B2B)

val NavyDark = KavachInk
val NavyMedium = KavachTeal
val GoldPrimary = Saffron
val CrimsonPrimary = SosRed
val CrimsonLight = Color(0xFFE26B6B)
val SafeGreen = Sage

val RakshaLightColors = lightColorScheme(
    primary = KavachTeal,
    onPrimary = Color.White,
    primaryContainer = TealTint,
    onPrimaryContainer = KavachInk,
    secondary = Sage,
    onSecondary = SageDeep,
    secondaryContainer = SageTint,
    onSecondaryContainer = SageDeep,
    tertiary = GuideViolet,
    onTertiary = Color.White,
    tertiaryContainer = VioletTint,
    onTertiaryContainer = VioletDeep,
    error = SosRed,
    onError = Color.White,
    errorContainer = SosTint,
    onErrorContainer = SosRed,
    background = Mist,
    onBackground = KavachInk,
    surface = Color.White,
    onSurface = KavachInk,
    surfaceVariant = DawnSand,
    onSurfaceVariant = InkSecondary,
    outline = Hairline,
    outlineVariant = Hairline
)