package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val SoftPinkBg = Color(0xFFFCE8F9)      // Pastel pink background
private val CrispWhite = Color(0xFFFFFFFF)      // Clean white cards & AI replies
private val SoftBlushTint = Color(0xFFF6DDF3)   // Soft blush for notice cards & chips
private val SoftRoseAccent = Color(0xFFC24D82)  // Lighter, warm rose (replaces dark purple)
private val DarkPlumText = Color(0xFF241729)    // High-contrast readable text
private val MutedPlumText = Color(0xFF5E4A63)   // Secondary text

@Composable
fun RakshaKavachTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            dynamicLightColorScheme(LocalContext.current)
        }
        else -> RakshaLightColors.copy(
            primary = SoftRoseAccent,
            onPrimary = Color.White,
            secondary = SoftRoseAccent,
            background = SoftPinkBg,
            onBackground = DarkPlumText,
            surface = CrispWhite,
            onSurface = DarkPlumText,
            surfaceVariant = SoftBlushTint,
            onSurfaceVariant = MutedPlumText,
            outline = Color(0xFFE5C8E2)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}