package com.guardem.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Guarde.me Design System - Premium Memory Capture Experience
 * Portuguese warmth meets modern voice-first design
 */

// ELEVATED COLOR SYSTEM
@Stable
class GuardeMeColorSystem {
    // Primary - Warm Portuguese Blue (Trust & Reliability)
    val primary = Color(0xFF2E5BBA)          // Deep ocean blue
    val primaryLight = Color(0xFF4A7BC8)     // Lighter accent
    val primaryContainer = Color(0xFFE8F0FF) // Soft container
    val onPrimary = Color(0xFFFFFFFF)
    val onPrimaryContainer = Color(0xFF1A365D)

    // Secondary - Memory Purple (Mind & Memory)
    val secondary = Color(0xFF8B5A9B)        // Gentle purple
    val secondaryLight = Color(0xFFA67BB0)   // Lighter variant
    val secondaryContainer = Color(0xFFF3E9F6) // Soft purple background
    val onSecondary = Color(0xFFFFFFFF)
    val onSecondaryContainer = Color(0xFF4A2C54)

    // Accent - Portuguese Gold (Warmth & Premium)
    val accent = Color(0xFFF4B942)           // Portuguese gold
    val accentLight = Color(0xFFF7C668)      // Lighter gold
    val accentContainer = Color(0xFFFEF7E6)  // Golden container
    val onAccent = Color(0xFF2D1B00)

    // Voice Recording States
    val voiceIdle = Color(0xFFE3F2FD)        // Calm blue
    val voiceListening = Color(0xFFFF6B6B)   // Warm red (active)
    val voiceProcessing = Color(0xFFFFB74D)  // Warm amber
    val voiceSuccess = Color(0xFF66BB6A)     // Success green
    val voiceError = Color(0xFFEF5350)       // Error red

    // Surface & Background
    val background = Color(0xFFFCFCFC)       // Warm white
    val surface = Color(0xFFFFFFFF)
    val surfaceVariant = Color(0xFFF8F9FA)   // Subtle variant
    val outline = Color(0xFFE1E5E9)
    val onBackground = Color(0xFF1A1A1A)
    val onSurface = Color(0xFF2E2E2E)

    // Semantic Colors
    val success = Color(0xFF2E7D32)
    val warning = Color(0xFFED6C02)
    val error = Color(0xFFD32F2F)
    val info = Color(0xFF0288D1)
}

// TYPOGRAPHY SYSTEM - Portuguese Warmth
@Stable
class GuardeMeTypography {
    // Headlines - For main titles and important content
    val displayLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.25).sp
    )
    
    val displayMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    )
    
    val headlineLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    )
    
    val headlineMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.15.sp
    )
    
    // Body - For main content
    val bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    
    val bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )
    
    val bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )
    
    // Labels - For buttons and small labels
    val labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    )
    
    val labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    
    val labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    
    // Voice-specific styles
    val voiceStatus = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    )
    
    val voiceInstruction = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    )
}

// SPACING SYSTEM - Consistent rhythm
@Stable
object GuardeMeSpacing {
    val xs = 4.dp     // Micro spacing
    val sm = 8.dp     // Small spacing
    val md = 16.dp    // Medium spacing (baseline)
    val lg = 24.dp    // Large spacing
    val xl = 32.dp    // Extra large
    val xxl = 48.dp   // Section spacing
    val xxxl = 64.dp  // Page spacing
    
    // Component-specific spacing
    val cardPadding = 20.dp
    val screenPadding = 20.dp
    val buttonPadding = 16.dp
    val iconSpacing = 12.dp
}

// ELEVATION SYSTEM - Subtle depth
@Stable
object GuardeMeElevation {
    val none = 0.dp
    val sm = 2.dp     // Cards
    val md = 4.dp     // Floating buttons
    val lg = 8.dp     // Modals
    val xl = 16.dp    // Dialogs
}

// SHAPE SYSTEM - Friendly curves
val GuardeMeShapes = Shapes(
    small = RoundedCornerShape(8.dp),      // Small components
    medium = RoundedCornerShape(16.dp),    // Cards, buttons
    large = RoundedCornerShape(24.dp)      // Large containers
)

// ANIMATION VALUES
@Stable
object GuardeMeAnimation {
    const val fast = 200
    const val medium = 350
    const val slow = 500
    
    // Voice-specific animations
    const val pulseAnimation = 1200
    const val breathingAnimation = 2000
    const val recordingRipple = 1500
}

// DESIGN TOKENS PROVIDER
@Stable
class GuardeMeDesignTokens {
    val colors = GuardeMeColorSystem()
    val typography = GuardeMeTypography()
    val spacing = GuardeMeSpacing
    val elevation = GuardeMeElevation
    val shapes = GuardeMeShapes
    val animation = GuardeMeAnimation
}

// GLOBAL DESIGN SYSTEM INSTANCE
val LocalGuardeMeDesign = GuardeMeDesignTokens()