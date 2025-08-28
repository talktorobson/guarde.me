package com.guardem.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guardem.app.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Premium UI Components for Guarde.me
 * Voice-first design with Portuguese warmth
 */

// PREMIUM MEMORY CARD
@Composable
fun PremiumMemoryCard(
    content: String,
    createdAt: String,
    tags: List<String>,
    source: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val design = LocalGuardeMeDesign
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .shadow(
                elevation = design.elevation.sm,
                shape = design.shapes.medium,
                spotColor = design.colors.primary.copy(alpha = 0.1f)
            ),
        shape = design.shapes.medium,
        backgroundColor = design.colors.surface,
        elevation = 0.dp // Using custom shadow instead
    ) {
        Column(
            modifier = Modifier.padding(design.spacing.cardPadding)
        ) {
            // Memory content with beautiful typography
            Text(
                text = content,
                style = design.typography.bodyLarge,
                color = design.colors.onSurface,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            
            if (tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(design.spacing.md))
                
                // Premium tag chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(design.spacing.sm),
                ) {
                    items(tags.take(3)) { tag ->
                        PremiumTagChip(
                            text = tag,
                            backgroundColor = design.colors.secondaryContainer,
                            textColor = design.colors.onSecondaryContainer
                        )
                    }
                    
                    if (tags.size > 3) {
                        item {
                            PremiumTagChip(
                                text = "+${tags.size - 3}",
                                backgroundColor = design.colors.primaryContainer,
                                textColor = design.colors.onPrimaryContainer
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(design.spacing.md))
            
            // Metadata with beautiful styling
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = createdAt,
                    style = design.typography.labelMedium,
                    color = design.colors.onSurface.copy(alpha = 0.7f)
                )
                
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = design.colors.accentContainer,
                    modifier = Modifier.padding(0.dp)
                ) {
                    Text(
                        text = source.uppercase(),
                        style = design.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 0.8.sp
                        ),
                        color = design.colors.onAccent,
                        modifier = Modifier.padding(
                            horizontal = design.spacing.sm,
                            vertical = design.spacing.xs
                        )
                    )
                }
            }
        }
    }
}

// PREMIUM TAG CHIP
@Composable
fun PremiumTagChip(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val design = LocalGuardeMeDesign
    
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            style = design.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = textColor,
            modifier = Modifier.padding(
                horizontal = design.spacing.sm,
                vertical = design.spacing.xs
            )
        )
    }
}

// VOICE RECORDING BUTTON WITH BREATHING ANIMATION
@Composable
fun VoiceRecordingButton(
    isRecording: Boolean,
    isProcessing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val design = LocalGuardeMeDesign
    
    // Breathing animation for idle state
    val infiniteTransition = rememberInfiniteTransition()
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    // Pulse animation for recording
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isRecording) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    // Processing rotation
    val processingRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (isProcessing) 360f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    val buttonColor = when {
        isRecording -> design.colors.voiceListening
        isProcessing -> design.colors.voiceProcessing
        else -> design.colors.primary
    }
    
    val scale = when {
        isRecording -> pulseScale
        !isRecording && !isProcessing -> breathScale
        else -> 1f
    }
    
    Box(
        modifier = modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer ring animation for recording
        if (isRecording) {
            VoiceRecordingRipples(
                color = design.colors.voiceListening.copy(alpha = 0.3f)
            )
        }
        
        // Main button
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
                .rotate(processingRotation),
            backgroundColor = buttonColor,
            elevation = FloatingActionButtonDefaults.elevation(
                defaultElevation = design.elevation.md,
                pressedElevation = design.elevation.lg
            )
        ) {
            Icon(
                imageVector = when {
                    isRecording -> Icons.Filled.Stop
                    isProcessing -> Icons.Filled.Settings
                    else -> Icons.Filled.Mic
                },
                contentDescription = when {
                    isRecording -> "Parar gravação"
                    isProcessing -> "Processando"
                    else -> "Iniciar gravação"
                },
                tint = design.colors.onPrimary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

// VOICE RECORDING RIPPLES ANIMATION
@Composable
fun VoiceRecordingRipples(
    color: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    
    val ripple1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    val ripple2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing, delayMillis = 500),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Canvas(
        modifier = modifier.size(120.dp)
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val maxRadius = size.width / 2f
        
        // First ripple
        if (ripple1 > 0f) {
            drawCircle(
                color = color.copy(alpha = (1f - ripple1) * 0.8f),
                radius = maxRadius * ripple1,
                center = center,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
            )
        }
        
        // Second ripple
        if (ripple2 > 0f) {
            drawCircle(
                color = color.copy(alpha = (1f - ripple2) * 0.6f),
                radius = maxRadius * ripple2,
                center = center,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
            )
        }
    }
}

// VOICE STATUS INDICATOR
@Composable
fun VoiceStatusIndicator(
    status: String,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val design = LocalGuardeMeDesign
    
    val backgroundColor = if (isActive) {
        design.colors.primaryContainer
    } else {
        design.colors.surfaceVariant
    }
    
    val textColor = if (isActive) {
        design.colors.onPrimaryContainer
    } else {
        design.colors.onSurface.copy(alpha = 0.7f)
    }
    
    Surface(
        modifier = modifier,
        shape = design.shapes.medium,
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = design.spacing.md,
                vertical = design.spacing.sm
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(design.spacing.sm)
        ) {
            // Status indicator dot
            Canvas(
                modifier = Modifier.size(8.dp)
            ) {
                drawCircle(
                    color = if (isActive) design.colors.primary else design.colors.outline,
                    radius = size.width / 2f
                )
            }
            
            Text(
                text = status,
                style = design.typography.voiceStatus,
                color = textColor
            )
        }
    }
}

// PREMIUM FLOATING ACTION BUTTON
@Composable
fun PremiumFloatingActionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = LocalGuardeMeDesign.colors.accent
) {
    val design = LocalGuardeMeDesign
    
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.shadow(
            elevation = design.elevation.md,
            shape = CircleShape,
            spotColor = backgroundColor.copy(alpha = 0.25f)
        ),
        backgroundColor = backgroundColor,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp) // Using custom shadow
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = design.colors.onAccent,
            modifier = Modifier.size(24.dp)
        )
    }
}

// EMPTY STATE ILLUSTRATION
@Composable
fun EmptyMemoriesIllustration(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    val design = LocalGuardeMeDesign
    
    Column(
        modifier = modifier.padding(design.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated illustration placeholder
        Canvas(
            modifier = Modifier.size(120.dp)
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            
            // Draw simple memory icon illustration
            drawCircle(
                color = design.colors.primaryContainer,
                radius = size.width / 2f,
                center = center
            )
            
            drawCircle(
                color = design.colors.primary,
                radius = size.width / 4f,
                center = center
            )
        }
        
        Spacer(modifier = Modifier.height(design.spacing.lg))
        
        Text(
            text = title,
            style = design.typography.headlineMedium,
            color = design.colors.onSurface
        )
        
        Spacer(modifier = Modifier.height(design.spacing.sm))
        
        Text(
            text = description,
            style = design.typography.bodyMedium,
            color = design.colors.onSurface.copy(alpha = 0.7f)
        )
    }
}

// LOADING STATE WITH PREMIUM ANIMATION
@Composable
fun PremiumLoadingIndicator(
    message: String,
    modifier: Modifier = Modifier
) {
    val design = LocalGuardeMeDesign
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = design.colors.primary,
            strokeWidth = 3.dp
        )
        
        Spacer(modifier = Modifier.height(design.spacing.md))
        
        Text(
            text = message,
            style = design.typography.bodyMedium,
            color = design.colors.onSurface.copy(alpha = 0.7f)
        )
    }
}