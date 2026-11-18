package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CrimsonLight,
    onPrimary = Color.White,
    primaryContainer = CrimsonDark,
    onPrimaryContainer = Color.White,
    secondary = GoldPrimary,
    onSecondary = NavyDark,
    secondaryContainer = NavyCard,
    onSecondaryContainer = NeutralTextLight,
    tertiary = SafeGreen,
    background = NavyDark,
    onBackground = NeutralTextLight,
    surface = NavyMedium,
    onSurface = NeutralTextLight,
    surfaceVariant = NavyCard,
    onSurfaceVariant = SlateAccent,
    error = CrimsonLight,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = CrimsonPrimary,
    onPrimary = Color.White,
    primaryContainer = CrimsonContainer,
    onPrimaryContainer = OnCrimsonContainer,
    secondary = NavyDark,
    onSecondary = Color.White,
    secondaryContainer = SurfaceCardLight,
    onSecondaryContainer = NeutralTextDark,
    tertiary = SafeGreen,
    background = SurfaceCream,
    onBackground = NeutralTextDark,
    surface = Color.White,
    onSurface = NeutralTextDark,
    surfaceVariant = Color(0xFFEDF2F7),
    onSurfaceVariant = NeutralTextSecondary,
    error = CrimsonPrimary,
    onError = Color.White
)

@Composable
fun RakshaKavachTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Preserve brand dignity and contrast for emergency app
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
