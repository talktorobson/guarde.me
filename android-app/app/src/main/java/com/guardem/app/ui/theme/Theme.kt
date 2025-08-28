package com.guardem.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

// Guarde.me Premium Design System Integration
private val GuardeMeLightColors = lightColors(
    primary = LocalGuardeMeDesign.colors.primary,
    primaryVariant = LocalGuardeMeDesign.colors.primaryLight,
    secondary = LocalGuardeMeDesign.colors.secondary,
    background = LocalGuardeMeDesign.colors.background,
    surface = LocalGuardeMeDesign.colors.surface,
    error = LocalGuardeMeDesign.colors.error,
    onPrimary = LocalGuardeMeDesign.colors.onPrimary,
    onSecondary = LocalGuardeMeDesign.colors.onSecondary,
    onBackground = LocalGuardeMeDesign.colors.onBackground,
    onSurface = LocalGuardeMeDesign.colors.onSurface,
    onError = LocalGuardeMeDesign.colors.onPrimary
)

private val GuardeMeDarkColors = darkColors(
    primary = LocalGuardeMeDesign.colors.primary,
    primaryVariant = LocalGuardeMeDesign.colors.primaryLight,
    secondary = LocalGuardeMeDesign.colors.secondary,
    background = LocalGuardeMeDesign.colors.onSurface,
    surface = LocalGuardeMeDesign.colors.onBackground,
    error = LocalGuardeMeDesign.colors.error,
    onPrimary = LocalGuardeMeDesign.colors.onPrimary,
    onSecondary = LocalGuardeMeDesign.colors.onSecondary,
    onBackground = LocalGuardeMeDesign.colors.background,
    onSurface = LocalGuardeMeDesign.colors.surface,
    onError = LocalGuardeMeDesign.colors.onPrimary
)

@Composable
fun GuardeMeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        GuardeMeDarkColors
    } else {
        GuardeMeLightColors
    }

    // Provide the premium design system
    CompositionLocalProvider(
        // LocalGuardeMeDesign provides LocalGuardeMeDesign
    ) {
        MaterialTheme(
            colors = colors,
            typography = GuardeMeMaterial2Typography,
            shapes = LocalGuardeMeDesign.shapes,
            content = content
        )
    }
}