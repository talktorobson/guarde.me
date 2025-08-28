package com.guardem.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.guardem.app.ui.theme.*

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val design = LocalGuardeMeDesign
    var autoSave by remember { mutableStateOf(true) }
    var offlineProcessing by remember { mutableStateOf(false) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var voiceQuality by remember { mutableStateOf("Alta") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        design.colors.background,
                        design.colors.surfaceVariant.copy(alpha = 0.3f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Premium Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                design.colors.primary,
                                design.colors.primaryLight
                            )
                        )
                    )
                    .padding(top = 48.dp) // Status bar padding
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(design.spacing.screenPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .background(
                                color = design.colors.onPrimary.copy(alpha = 0.1f),
                                shape = design.shapes.small
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = design.colors.onPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(design.spacing.md))
                    
                    Column {
                        Text(
                            text = "Configurações",
                            style = design.typography.displayMedium,
                            color = design.colors.onPrimary
                        )
                        Text(
                            text = "Personalize sua experiência",
                            style = design.typography.bodyMedium,
                            color = design.colors.onPrimary.copy(alpha = 0.8f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(design.spacing.md))
            }
        
            // Settings content with premium styling
            Column(
                modifier = Modifier.padding(design.spacing.screenPadding),
                verticalArrangement = Arrangement.spacedBy(design.spacing.lg)
            ) {
                // Voice Settings Section
                SettingsSection(
                    title = "Configurações de Voz",
                    icon = Icons.Default.RecordVoiceOver
                ) {
                    SettingsToggle(
                        title = "Auto-salvamento",
                        description = "Salva automaticamente suas gravações",
                        checked = autoSave,
                        onCheckedChange = { autoSave = it }
                    )
                    
                    SettingsToggle(
                        title = "Processamento offline",
                        description = "Processa memórias sem conexão",
                        checked = offlineProcessing,
                        onCheckedChange = { offlineProcessing = it }
                    )
                    
                    SettingsDropdown(
                        title = "Qualidade de áudio",
                        description = "Define a qualidade das gravações",
                        value = voiceQuality,
                        options = listOf("Baixa", "Média", "Alta", "Premium"),
                        onValueChange = { voiceQuality = it }
                    )
                }
                
                // Notification Settings Section
                SettingsSection(
                    title = "Notificações",
                    icon = Icons.Default.Notifications
                ) {
                    SettingsToggle(
                        title = "Notificações push",
                        description = "Receba lembretes de suas memórias",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                }
                
                // Privacy & Security Section
                SettingsSection(
                    title = "Privacidade e Segurança",
                    icon = Icons.Default.Security
                ) {
                    SettingsButton(
                        title = "Política de Privacidade",
                        description = "Veja como protegemos seus dados",
                        icon = Icons.Default.PrivacyTip,
                        onClick = { /* Navigate to privacy policy */ }
                    )
                    
                    SettingsButton(
                        title = "Termos de Uso",
                        description = "Leia nossos termos de serviço",
                        icon = Icons.Default.Description,
                        onClick = { /* Navigate to terms */ }
                    )
                }
                
                // About Section
                SettingsSection(
                    title = "Sobre o App",
                    icon = Icons.Default.Info
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = design.spacing.sm),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Guarde.me v1.0",
                                style = design.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = design.colors.onSurface
                            )
                            Text(
                                text = "App para captura de memórias com voz e IA",
                                style = design.typography.bodyMedium,
                                color = design.colors.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        
                        Surface(
                            shape = design.shapes.small,
                            color = design.colors.primaryContainer
                        ) {
                            Text(
                                text = "BETA",
                                style = design.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = design.colors.onPrimaryContainer,
                                modifier = Modifier.padding(
                                    horizontal = design.spacing.sm,
                                    vertical = design.spacing.xs
                                )
                            )
                        }
                    }
                }
            
                }
            
                Spacer(modifier = Modifier.height(design.spacing.xl))
                
                // Danger Zone
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = design.shapes.medium,
                    backgroundColor = design.colors.voiceError.copy(alpha = 0.05f),
                    elevation = design.elevation.sm
                ) {
                    Column(
                        modifier = Modifier.padding(design.spacing.cardPadding)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(design.spacing.sm)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = design.colors.voiceError,
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Text(
                                text = "Zona de Perigo",
                                style = design.typography.headlineMedium,
                                color = design.colors.voiceError
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(design.spacing.md))
                        
                        Button(
                            onClick = onNavigateToLogin,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = design.colors.voiceError
                            ),
                            shape = design.shapes.medium
                        ) {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(design.spacing.sm))
                            
                            Text(
                                text = "Sair do App",
                                style = design.typography.labelLarge,
                                color = design.colors.onPrimary
                            )
                        }
                    }
                }
                
                // Bottom padding
                Spacer(modifier = Modifier.height(design.spacing.xxxl))
            }
        }
    }
}

// Premium Settings Components
@Composable
fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    val design = LocalGuardeMeDesign
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = design.shapes.medium,
        backgroundColor = design.colors.surface,
        elevation = design.elevation.sm
    ) {
        Column(
            modifier = Modifier.padding(design.spacing.cardPadding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(design.spacing.sm)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = design.colors.primary,
                    modifier = Modifier.size(24.dp)
                )
                
                Text(
                    text = title,
                    style = design.typography.headlineMedium,
                    color = design.colors.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(design.spacing.md))
            
            content()
        }
    }
}

@Composable
fun SettingsToggle(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val design = LocalGuardeMeDesign
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = design.spacing.sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = design.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = design.colors.onSurface
            )
            Text(
                text = description,
                style = design.typography.bodyMedium,
                color = design.colors.onSurface.copy(alpha = 0.7f)
            )
        }
        
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = design.colors.primary,
                checkedTrackColor = design.colors.primaryContainer
            )
        )
    }
}

@Composable
fun SettingsButton(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    val design = LocalGuardeMeDesign
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = design.spacing.xs),
        shape = design.shapes.small,
        color = design.colors.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(design.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(design.spacing.md)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = design.colors.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = design.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = design.colors.onSurface
                )
                Text(
                    text = description,
                    style = design.typography.bodyMedium,
                    color = design.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = design.colors.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun SettingsDropdown(
    title: String,
    description: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    val design = LocalGuardeMeDesign
    var expanded by remember { mutableStateOf(false) }
    
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = design.spacing.sm),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = design.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = design.colors.onSurface
                )
                Text(
                    text = description,
                    style = design.typography.bodyMedium,
                    color = design.colors.onSurface.copy(alpha = 0.7f)
                )
            }
            
            Surface(
                shape = design.shapes.small,
                color = design.colors.primaryContainer
            ) {
                Text(
                    text = value,
                    style = design.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = design.colors.onPrimaryContainer,
                    modifier = Modifier.padding(
                        horizontal = design.spacing.md,
                        vertical = design.spacing.sm
                    )
                )
            }
        }
        
        // Note: In a real implementation, you'd add a proper dropdown menu here
    }
}