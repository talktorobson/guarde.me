package com.guardem.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guardem.app.data.speech.SpeechRecognitionService
import com.guardem.app.ui.components.*
import com.guardem.app.ui.screens.voice.VoiceCaptureViewModel
import com.guardem.app.ui.screens.voice.VoiceRecordingState
import com.guardem.app.ui.theme.*

@Composable
fun VoiceCaptureScreen(
    onNavigateToMemories: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    val speechService = remember { SpeechRecognitionService(context) }
    val viewModel: VoiceCaptureViewModel = viewModel { VoiceCaptureViewModel(speechService) }
    val uiState by viewModel.uiState.collectAsState()
    val design = LocalGuardeMeDesign
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        design.colors.primaryContainer.copy(alpha = 0.1f),
                        design.colors.background,
                        design.colors.background
                    ),
                    radius = 800f
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(design.spacing.screenPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status bar spacing
            Spacer(modifier = Modifier.height(48.dp))
            
            // Premium header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = design.spacing.lg)
            ) {
                Text(
                    text = "Guarde sua Memória",
                    style = design.typography.displayMedium,
                    color = design.colors.onBackground,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(design.spacing.sm))
                
                Text(
                    text = "Diga \"Guarde me\" seguido de sua memória",
                    style = design.typography.bodyMedium,
                    color = design.colors.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(design.spacing.xxl))
            // Premium voice status indicator
            VoiceStatusIndicator(
                status = when (uiState.recordingState) {
                    VoiceRecordingState.Idle -> "Pronto para gravar"
                    VoiceRecordingState.Recording -> "Ouvindo... fale agora!"
                    VoiceRecordingState.Processing -> "Processando com IA..."
                    VoiceRecordingState.Success -> "Processamento concluído!"
                    VoiceRecordingState.Error -> "Erro no processamento"
                },
                isActive = uiState.recordingState != VoiceRecordingState.Idle,
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        
            Spacer(modifier = Modifier.height(design.spacing.xxl))
            
            // Premium voice recording button with breathing animation
            VoiceRecordingButton(
                isRecording = uiState.recordingState == VoiceRecordingState.Recording,
                isProcessing = uiState.recordingState == VoiceRecordingState.Processing,
                onClick = {
                    when (uiState.recordingState) {
                        VoiceRecordingState.Idle -> viewModel.startRecording()
                        VoiceRecordingState.Recording -> viewModel.stopRecording()
                        else -> { /* Do nothing during processing */ }
                    }
                }
            )
        
            Spacer(modifier = Modifier.height(design.spacing.xxl))
            
            // Dynamic instruction text with animations
            AnimatedContent(
                targetState = uiState.recordingState,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) with fadeOut(animationSpec = tween(300))
                }
            ) { state ->
                Card(
                    modifier = Modifier.fillMaxWidth(0.9f),
                    shape = design.shapes.medium,
                    backgroundColor = when (state) {
                        VoiceRecordingState.Recording -> design.colors.voiceListening.copy(alpha = 0.1f)
                        VoiceRecordingState.Processing -> design.colors.voiceProcessing.copy(alpha = 0.1f)
                        else -> design.colors.primaryContainer.copy(alpha = 0.3f)
                    },
                    elevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.padding(design.spacing.cardPadding),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (state) {
                            VoiceRecordingState.Recording -> {
                                Icon(
                                    imageVector = Icons.Default.RecordVoiceOver,
                                    contentDescription = null,
                                    tint = design.colors.voiceListening,
                                    modifier = Modifier.size(32.dp)
                                )
                                
                                Spacer(modifier = Modifier.height(design.spacing.sm))
                                
                                Text(
                                    text = "Fale sobre sua memória...",
                                    style = design.typography.headlineMedium,
                                    color = design.colors.onSurface,
                                    textAlign = TextAlign.Center
                                )
                                
                                Text(
                                    text = "Diga 'Guarde me...' seguido do que deseja lembrar",
                                    style = design.typography.bodyMedium,
                                    color = design.colors.onSurface.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                            
                            VoiceRecordingState.Processing -> {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = design.colors.voiceProcessing,
                                    modifier = Modifier.size(32.dp)
                                )
                                
                                Spacer(modifier = Modifier.height(design.spacing.sm))
                                
                                Text(
                                    text = "Processando...",
                                    style = design.typography.headlineMedium,
                                    color = design.colors.onSurface,
                                    textAlign = TextAlign.Center
                                )
                                
                                Text(
                                    text = "Nossa IA está analisando sua memória",
                                    style = design.typography.bodyMedium,
                                    color = design.colors.onSurface.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                            
                            else -> {
                                Icon(
                                    imageVector = Icons.Default.TouchApp,
                                    contentDescription = null,
                                    tint = design.colors.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                                
                                Spacer(modifier = Modifier.height(design.spacing.sm))
                                
                                Text(
                                    text = "Toque para começar",
                                    style = design.typography.headlineMedium,
                                    color = design.colors.onSurface,
                                    textAlign = TextAlign.Center
                                )
                                
                                Text(
                                    text = "Toque no microfone para gravar sua memória",
                                    style = design.typography.bodyMedium,
                                    color = design.colors.onSurface.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        
            // Real-time transcript display with premium styling
            AnimatedVisibility(
                visible = uiState.transcript.isNotEmpty(),
                enter = slideInVertically(animationSpec = tween(300)) + 
                       fadeIn(animationSpec = tween(300))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(design.spacing.lg))
                    
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
                                    imageVector = Icons.Default.Hearing,
                                    contentDescription = null,
                                    tint = design.colors.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                
                                Text(
                                    text = "Reconhecimento em tempo real",
                                    style = design.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = design.colors.primary
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(design.spacing.sm))
                            
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                shape = design.shapes.small,
                                color = design.colors.primaryContainer.copy(alpha = 0.3f)
                            ) {
                                Text(
                                    text = "\"${uiState.transcript}\"",
                                    style = design.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = design.colors.onSurface,
                                    modifier = Modifier.padding(design.spacing.md)
                                )
                            }
                        }
                    }
                }
            }
        
            // AI processing result with premium styling
            AnimatedVisibility(
                visible = uiState.intentResult != null,
                enter = slideInVertically(animationSpec = tween(400)) + 
                       fadeIn(animationSpec = tween(400))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(design.spacing.lg))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = design.shapes.medium,
                        backgroundColor = when (uiState.recordingState) {
                            VoiceRecordingState.Success -> design.colors.voiceSuccess.copy(alpha = 0.1f)
                            else -> design.colors.voiceError.copy(alpha = 0.1f)
                        },
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
                                    imageVector = if (uiState.recordingState == VoiceRecordingState.Success) 
                                        Icons.Default.CheckCircle else Icons.Default.Error,
                                    contentDescription = null,
                                    tint = when (uiState.recordingState) {
                                        VoiceRecordingState.Success -> design.colors.voiceSuccess
                                        else -> design.colors.voiceError
                                    },
                                    modifier = Modifier.size(20.dp)
                                )
                                
                                Text(
                                    text = if (uiState.recordingState == VoiceRecordingState.Success) 
                                        "Memória processada" else "Erro no processamento",
                                    style = design.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = when (uiState.recordingState) {
                                        VoiceRecordingState.Success -> design.colors.voiceSuccess
                                        else -> design.colors.voiceError
                                    }
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(design.spacing.sm))
                            
                            Text(
                                text = uiState.intentResult ?: "",
                                style = design.typography.bodyMedium,
                                color = design.colors.onSurface
                            )
                        }
                    }
                }
            }
        
            // Error message with premium styling
            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter = slideInVertically(animationSpec = tween(300)) + 
                       fadeIn(animationSpec = tween(300))
            ) {
                Column {
                    Spacer(modifier = Modifier.height(design.spacing.lg))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = design.shapes.medium,
                        backgroundColor = design.colors.voiceError.copy(alpha = 0.1f),
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
                                    text = "Algo deu errado",
                                    style = design.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = design.colors.voiceError
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(design.spacing.sm))
                            
                            Text(
                                text = uiState.errorMessage ?: "",
                                style = design.typography.bodyMedium,
                                color = design.colors.onSurface
                            )
                        }
                    }
                }
            }
        
            // Bottom spacing and actions
            Spacer(modifier = Modifier.height(design.spacing.xxl))
            
            // Premium navigation buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(design.spacing.md)
            ) {
                OutlinedButton(
                    onClick = onNavigateToMemories,
                    modifier = Modifier.weight(1f),
                    shape = design.shapes.medium,
                    border = ButtonDefaults.outlinedBorder.copy(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                design.colors.primary,
                                design.colors.primaryLight
                            )
                        )
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(design.spacing.sm))
                    
                    Text(
                        text = "Ver Memórias",
                        style = design.typography.labelLarge
                    )
                }
                
                OutlinedButton(
                    onClick = onNavigateToSettings,
                    modifier = Modifier.weight(1f),
                    shape = design.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(design.spacing.sm))
                    
                    Text(
                        text = "Configurações",
                        style = design.typography.labelLarge
                    )
                }
            }
            
            // Bottom padding
            Spacer(modifier = Modifier.height(design.spacing.xl))
        }
    }
}