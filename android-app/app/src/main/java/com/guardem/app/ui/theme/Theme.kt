package com.guardem.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Guarde.me Brand Colors
private val GuardeMeLightColorScheme = lightColorScheme(
    primary = GuardeMePrimary,
    onPrimary = GuardeMeOnPrimary,
    primaryContainer = GuardeMePrimaryContainer,
    onPrimaryContainer = GuardeMeOnPrimaryContainer,
    secondary = GuardeMeSecondary,
    onSecondary = GuardeMeOnSecondary,
    secondaryContainer = GuardeMeSecondaryContainer,
    onSecondaryContainer = GuardeMeOnSecondaryContainer,
    tertiary = GuardeMeTertiary,
    onTertiary = GuardeMeOnTertiary,
    tertiaryContainer = GuardeMeTertiaryContainer,
    onTertiaryContainer = GuardeMeOnTertiaryContainer,
    error = GuardeMeError,
    onError = GuardeMeOnError,
    errorContainer = GuardeMeErrorContainer,
    onErrorContainer = GuardeMeOnErrorContainer,
    background = GuardeMeBackground,
    onBackground = GuardeMeOnBackground,
    surface = GuardeMeSurface,
    onSurface = GuardeMeOnSurface,
    surfaceVariant = GuardeMeSurfaceVariant,
    onSurfaceVariant = GuardeMeOnSurfaceVariant,
    outline = GuardeMeOutline,
    outlineVariant = GuardeMeOutlineVariant,
    scrim = GuardeMeScrim
)

private val GuardeMeDarkColorScheme = darkColorScheme(
    primary = GuardeMePrimaryDark,
    onPrimary = GuardeMeOnPrimaryDark,
    primaryContainer = GuardeMePrimaryContainerDark,
    onPrimaryContainer = GuardeMeOnPrimaryContainerDark,
    secondary = GuardeMeSecondaryDark,
    onSecondary = GuardeMeOnSecondaryDark,
    secondaryContainer = GuardeMeSecondaryContainerDark,
    onSecondaryContainer = GuardeMeOnSecondaryContainerDark,
    tertiary = GuardeMeTertiaryDark,
    onTertiary = GuardeMeOnTertiaryDark,
    tertiaryContainer = GuardeMeTertiaryContainerDark,
    onTertiaryContainer = GuardeMeOnTertiaryContainerDark,
    error = GuardeMeErrorDark,
    onError = GuardeMeOnErrorDark,
    errorContainer = GuardeMeErrorContainerDark,
    onErrorContainer = GuardeMeOnErrorContainerDark,
    background = GuardeMeBackgroundDark,
    onBackground = GuardeMeOnBackgroundDark,
    surface = GuardeMeSurfaceDark,
    onSurface = GuardeMeOnSurfaceDark,
    surfaceVariant = GuardeMeSurfaceVariantDark,
    onSurfaceVariant = GuardeMeOnSurfaceVariantDark,
    outline = GuardeMeOutlineDark,
    outlineVariant = GuardeMeOutlineVariantDark,
    scrim = GuardeMeScrimDark
)

@Composable
fun GuardeMeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> GuardeMeDarkColorScheme
        else -> GuardeMeLightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = GuardeMeTypography,
        content = content
    )
}