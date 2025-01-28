package com.example.smartconciergeapp.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Teal200,
    secondary = Teal700,
    background = DarkGray,
    onBackground = White,
)

private val LightColorScheme = lightColorScheme(
    primary = Purple500,
    secondary = Purple700,
    background = White,
    onBackground = Black,
)

@Composable
fun SmartConciergeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
