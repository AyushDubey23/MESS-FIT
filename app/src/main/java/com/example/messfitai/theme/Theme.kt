package com.example.messfitai.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MessFitColorScheme = darkColorScheme(
    primary = NeonGreen,
    secondary = NeonGreenLight,
    background = DarkBackground,
    surface = CardBackground,
    onPrimary = DarkBackground,
    onSecondary = DarkBackground,
    onBackground = TextWhite,
    onSurface = TextWhite,
    outline = CardBorder
)

@Composable
fun MESSFITAITheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MessFitColorScheme,
        typography = Typography,
        content = content
    )
}
