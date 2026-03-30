package com.siam.ai.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = AccentGreen,
    secondary = AccentBlue,
    tertiary = AccentPurple,
    background = DarkBg,
    surface = SurfaceDark,
    surfaceVariant = CardDark,
    onBackground = TextLight,
    onSurface = TextLight,
    onPrimary = DarkBg,
    onSecondary = DarkBg,
    onTertiary = DarkBg
)

@Composable
fun SiamAiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
