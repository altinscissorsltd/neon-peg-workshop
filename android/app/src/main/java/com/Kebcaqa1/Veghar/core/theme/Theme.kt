package com.Kebcaqa1.Veghar.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NeonScheme = darkColorScheme(
    primary = AccentMagenta,
    onPrimary = TextPrimary,
    primaryContainer = AccentMagentaDeep,
    onPrimaryContainer = TextPrimary,
    secondary = AccentCyan,
    onSecondary = BgBase,
    secondaryContainer = SurfaceAccent,
    onSecondaryContainer = AccentCyan,
    tertiary = AccentMagentaLight,
    onTertiary = BgBase,
    background = BgBase,
    onBackground = TextPrimary,
    surface = SurfaceDim,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    outline = DividerSpent,
    outlineVariant = TextMuted,
    error = AccentMagentaLight,
    onError = TextPrimary,
    scrim = BgDeep
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NeonScheme,
        typography = AppTypography,
        content = content
    )
}
