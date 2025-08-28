package com.guardem.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
// import androidx.hilt.navigation.compose.hiltViewModel
// import com.guardem.app.ui.screens.auth.AuthViewModel
import com.guardem.app.ui.theme.*

@Composable
fun LoginScreen(
    onNavigateToVoice: () -> Unit
) {
    // Temporarily disable AuthViewModel for build fix
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val design = LocalGuardeMeDesign

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        design.colors.primary.copy(alpha = 0.1f),
                        design.colors.background,
                        design.colors.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(design.spacing.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo and Title
            Text(
                text = "Guarde.me",
                style = design.typography.displayLarge,
                color = design.colors.primary,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(design.spacing.sm))
            
            Text(
                text = "Memórias com Voz e IA",
                style = design.typography.headlineMedium,
                color = design.colors.onBackground.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(design.spacing.xxl))

            // Login Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = design.spacing.md),
                shape = design.shapes.medium,
                backgroundColor = design.colors.surface,
                elevation = design.elevation.sm
            ) {
                Column(
                    modifier = Modifier.padding(design.spacing.cardPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Entre no Guarde.me",
                        style = design.typography.headlineMedium,
                        color = design.colors.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(design.spacing.lg))

                    // Google Sign In Button
                    Button(
                        onClick = { 
                            // Temporarily simulate login
                            onNavigateToVoice()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = design.colors.primary
                        ),
                        shape = design.shapes.medium
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = design.colors.onPrimary,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Login,
                                    contentDescription = null,
                                    tint = design.colors.onPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                
                                Spacer(modifier = Modifier.width(design.spacing.sm))
                                
                                Text(
                                    text = "Continuar com Google",
                                    style = design.typography.labelLarge,
                                    color = design.colors.onPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(design.spacing.md))

                    // Guest Entry Button
                    OutlinedButton(
                        onClick = { 
                            // Temporarily simulate guest login
                            onNavigateToVoice()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isLoading,
                        shape = design.shapes.medium
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(design.spacing.sm))
                            
                            Text(
                                text = "Continuar sem cadastro",
                                style = design.typography.labelLarge
                            )
                        }
                    }

                    // Error Message
                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(design.spacing.md))
                        Card(
                            backgroundColor = design.colors.voiceError.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth(),
                            shape = design.shapes.small
                        ) {
                            Text(
                                text = errorMessage ?: "",
                                style = design.typography.bodyMedium,
                                color = design.colors.voiceError,
                                modifier = Modifier.padding(design.spacing.md)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(design.spacing.xl))

            // Features Preview
            Text(
                text = "✨ Recursos principais:",
                style = design.typography.headlineMedium,
                color = design.colors.onBackground
            )

            Spacer(modifier = Modifier.height(design.spacing.md))

            Column(
                modifier = Modifier.padding(horizontal = design.spacing.md)
            ) {
                FeatureItem("🎙️", "Comando de voz \"Guarde me\"")
                FeatureItem("🧠", "IA para entender suas memórias")
                FeatureItem("⏰", "Entrega inteligente no momento certo")
                FeatureItem("📱", "Notificações personalizadas")
            }
        }
    }

    // Navigation is now handled directly in onClick callbacks
}

@Composable
private fun FeatureItem(icon: String, text: String) {
    val design = LocalGuardeMeDesign
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = design.spacing.xs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            style = design.typography.bodyLarge,
            modifier = Modifier.width(32.dp)
        )
        Text(
            text = text,
            style = design.typography.bodyMedium,
            color = design.colors.onBackground.copy(alpha = 0.8f)
        )
    }
}