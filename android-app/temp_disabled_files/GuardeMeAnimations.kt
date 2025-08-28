package com.guardem.app.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.guardem.app.ui.theme.LocalGuardeMeDesign
import kotlin.math.*

/**
 * Guarde.me Animation Library
 * Premium animations for voice-first memory capture experience
 */

// VOICE WAVE VISUALIZATION
@Composable
fun VoiceWaveAnimation(
    isActive: Boolean,
    amplitude: Float = 1f,
    color: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    
    val wave1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    val wave2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    val wave3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Canvas(
        modifier = modifier.size(width = 200.dp, height = 60.dp)
    ) {
        val centerY = size.height / 2
        val waveWidth = size.width
        
        for (x in 0..waveWidth.toInt() step 4) {
            val normalizedX = x / waveWidth
            
            val wave1Y = sin(wave1 + normalizedX * 4 * PI).toFloat() * amplitude * 15
            val wave2Y = sin(wave2 + normalizedX * 6 * PI + PI/3).toFloat() * amplitude * 10
            val wave3Y = sin(wave3 + normalizedX * 8 * PI + 2*PI/3).toFloat() * amplitude * 5
            
            val combinedY = (wave1Y + wave2Y + wave3Y) / 3
            
            if (isActive) {
                drawCircle(
                    color = color.copy(alpha = 0.8f - (abs(combinedY) / 20f)),
                    radius = 2f + abs(combinedY) / 5f,
                    center = Offset(x.toFloat(), centerY + combinedY)
                )
            } else {
                drawCircle(
                    color = color.copy(alpha = 0.3f),
                    radius = 1f,
                    center = Offset(x.toFloat(), centerY)
                )
            }
        }
    }
}

// MEMORY CARD REVEAL ANIMATION
@Composable
fun MemoryCardRevealAnimation(
    visible: Boolean,
    delayMillis: Int = 0,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(
                durationMillis = 600,
                delayMillis = delayMillis,
                easing = FastOutSlowInEasing
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = 400,
                delayMillis = delayMillis + 200,
                easing = LinearOutSlowInEasing
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(
                durationMillis = 300,
                easing = FastOutLinearInEasing
            )
        ) + fadeOut(
            animationSpec = tween(
                durationMillis = 200,
                easing = LinearOutSlowInEasing
            )
        )
    ) {
        content()
    }
}

// FLOATING ELEMENT ANIMATION
@Composable
fun FloatingAnimation(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = modifier.offset(y = offsetY.dp)
    ) {
        content()
    }
}

// SHIMMER LOADING ANIMATION
@Composable
fun ShimmerAnimation(
    modifier: Modifier = Modifier,
    color: Color = LocalGuardeMeDesign.colors.primaryContainer.copy(alpha = 0.3f)
) {
    val infiniteTransition = rememberInfiniteTransition()
    
    val shimmerTranslate by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Canvas(modifier = modifier) {
        val brush = Brush.linearGradient(
            colors = listOf(
                color.copy(alpha = 0.1f),
                color.copy(alpha = 0.8f),
                color.copy(alpha = 0.1f)
            ),
            start = Offset(shimmerTranslate - 150f, 0f),
            end = Offset(shimmerTranslate + 150f, 0f)
        )
        
        drawRect(brush = brush, size = size)
    }
}

// BOUNCE ANIMATION
@Composable
fun BounceAnimation(
    targetScale: Float = 0.95f,
    content: @Composable (Modifier) -> Unit
) {
    var pressed by remember { mutableStateOf(false) }
    
    val scale by animateFloatAsState(
        targetValue = if (pressed) targetScale else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    content(Modifier.scale(scale))
}

// SCREEN TRANSITION ANIMATIONS
object GuardeMeTransitions {
    val slideInFromRight = slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(400, easing = FastOutSlowInEasing)
    ) + fadeIn(animationSpec = tween(400))
    
    val slideOutToLeft = slideOutHorizontally(
        targetOffsetX = { -it },
        animationSpec = tween(400, easing = FastOutSlowInEasing)
    ) + fadeOut(animationSpec = tween(400))
    
    val fadeInSlowly = fadeIn(
        animationSpec = tween(600, easing = LinearOutSlowInEasing)
    )
    
    val scaleInBounce = scaleIn(
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
    ) + fadeIn()
}