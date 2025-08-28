package com.guardem.app.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.guardem.app.ui.theme.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val design = LocalGuardeMeDesign
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    
    // Premium bottom navigation with glass morphism effect
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = design.elevation.lg,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                spotColor = design.colors.primary.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        backgroundColor = design.colors.surface.copy(alpha = 0.95f),
        elevation = 0.dp // Using custom shadow
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            design.colors.surface.copy(alpha = 0.9f),
                            design.colors.primaryContainer.copy(alpha = 0.1f),
                            design.colors.surface.copy(alpha = 0.9f)
                        )
                    )
                )
                .padding(
                    horizontal = design.spacing.md,
                    vertical = design.spacing.md
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentRoute == item.route
                val isVoiceTab = item.route == GuardeMeDestinations.VoiceCapture
                
                PremiumNavItem(
                    item = item,
                    isSelected = isSelected,
                    isVoiceTab = isVoiceTab,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PremiumNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    isVoiceTab: Boolean,
    onClick: () -> Unit
) {
    val design = LocalGuardeMeDesign
    
    // Animation for selection state
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    
    val animatedElevation by animateDpAsState(
        targetValue = if (isSelected) design.elevation.md else design.elevation.none,
        animationSpec = tween(300)
    )
    
    if (isVoiceTab) {
        // Special voice tab with floating design
        Box(
            modifier = Modifier
                .size(64.dp)
                .scale(animatedScale),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .shadow(
                        elevation = animatedElevation + design.elevation.sm,
                        shape = CircleShape,
                        spotColor = design.colors.accent.copy(alpha = 0.3f)
                    )
                    .clickable { onClick() },
                shape = CircleShape,
                color = if (isSelected) design.colors.accent else design.colors.primary,
                elevation = 0.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Background gradient for voice tab
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.2f),
                                        Color.Transparent
                                    ),
                                    radius = 28.dp.value
                                ),
                                shape = CircleShape
                            )
                    )
                    
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = design.colors.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    } else {
        // Regular navigation items with premium styling
        Column(
            modifier = Modifier
                .padding(horizontal = design.spacing.sm)
                .scale(animatedScale)
                .selectable(
                    selected = isSelected,
                    onClick = onClick,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(design.spacing.xs)
        ) {
            // Icon container with animated background
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (isSelected) design.colors.primaryContainer else Color.Transparent,
                        shape = design.shapes.small
                    )
                    .border(
                        width = if (isSelected) 1.dp else 0.dp,
                        color = if (isSelected) design.colors.primary.copy(alpha = 0.3f) else Color.Transparent,
                        shape = design.shapes.small
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.label,
                    tint = if (isSelected) design.colors.primary else design.colors.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
            
            // Label with animated appearance
            AnimatedVisibility(
                visible = isSelected,
                enter = slideInVertically(animationSpec = tween(200)) + 
                       fadeIn(animationSpec = tween(200)),
                exit = slideOutVertically(animationSpec = tween(200)) + 
                      fadeOut(animationSpec = tween(200))
            ) {
                Text(
                    text = item.label,
                    style = design.typography.labelSmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = design.colors.primary,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

private val bottomNavItems = listOf(
    BottomNavItem(
        route = GuardeMeDestinations.Memories,
        icon = Icons.Default.History,
        label = "Memórias"
    ),
    BottomNavItem(
        route = GuardeMeDestinations.VoiceCapture,
        icon = Icons.Default.Mic,
        label = "Gravar"
    ),
    BottomNavItem(
        route = GuardeMeDestinations.Settings,
        icon = Icons.Default.Settings,
        label = "Config"
    )
)

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)