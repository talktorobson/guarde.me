package com.guardem.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

// Guarde.me Brand Colors for Material 2
private val GuardeMeLightColors = lightColors(
    primary = GuardeMePrimary,
    primaryVariant = GuardeMePrimaryVariant,
    secondary = GuardeMeSecondary,
    background = GuardeMeBackground,
    surface = GuardeMeSurface,
    error = GuardeMeError,
    onPrimary = GuardeMeOnPrimary,
    onSecondary = GuardeMeOnSecondary,
    onBackground = GuardeMeOnBackground,
    onSurface = GuardeMeOnSurface,
    onError = GuardeMeOnError
)

private val GuardeMeDarkColors = darkColors(
    primary = GuardeMePrimaryDark,
    primaryVariant = GuardeMePrimaryVariantDark,
    secondary = GuardeMeSecondaryDark,
    background = GuardeMeBackgroundDark,
    surface = GuardeMeSurfaceDark,
    error = GuardeMeErrorDark,
    onPrimary = GuardeMeOnPrimaryDark,
    onSecondary = GuardeMeOnSecondaryDark,
    onBackground = GuardeMeOnBackgroundDark,
    onSurface = GuardeMeOnSurfaceDark,
    onError = GuardeMeOnErrorDark
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

    MaterialTheme(
        colors = colors,
        typography = GuardeMeTypography,
        content = content
    )
}