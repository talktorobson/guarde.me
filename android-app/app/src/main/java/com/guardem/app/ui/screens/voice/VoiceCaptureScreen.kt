package com.guardem.app.ui.screens.voice

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guardem.app.ui.theme.VoiceRecordingActive
import com.guardem.app.ui.theme.VoiceRecordingIdle
import com.guardem.app.ui.theme.VoiceRecordingProcessing
import com.guardem.app.ui.theme.VoiceRecordingSuccess

@Composable
fun VoiceCaptureScreen(
    onNavigateToMemories: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    viewModel: VoiceCaptureViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Top Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FloatingActionButton(
                onClick = onNavigateToMemories,
                modifier = Modifier.size(48.dp),
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Ver memórias",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            FloatingActionButton(
                onClick = onNavigateToSettings,
                modifier = Modifier.size(48.dp),
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configurações",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // App Title
        Text(
            text = "🎙️ Guarde.me",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Diga \"Guarde me\" seguido da sua memória",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Voice Recording Visualization
        VoiceRecordingVisualizer(
            state = uiState.recordingState,
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Recording Status Text
        Text(
            text = when (uiState.recordingState) {
                VoiceRecordingState.Idle -> "Toque no microfone para começar"
                VoiceRecordingState.Listening -> "Ouvindo... Diga \"Guarde me\""
                VoiceRecordingState.Recording -> "Gravando sua memória..."
                VoiceRecordingState.Processing -> "Processando com IA..."
                VoiceRecordingState.Success -> "Memória capturada com sucesso!"
                VoiceRecordingState.Error -> "Erro: ${uiState.errorMessage ?: "Tente novamente"}"
            },
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Transcript Display
        if (uiState.transcript.isNotEmpty()) {
            Card(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Transcrição:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.transcript,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Intent Display
        uiState.intentResult?.let { intent ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "IA Entendeu:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tipo: ${intent.slots.contentType}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    intent.slots.whenValue?.let {
                        Text(
                            text = "Quando: ${it}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = "Canal: ${intent.slots.channel}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Recording Button
        FloatingActionButton(
            onClick = {
                when (uiState.recordingState) {
                    VoiceRecordingState.Idle -> viewModel.startListening()
                    VoiceRecordingState.Listening -> viewModel.stopListening()
                    VoiceRecordingState.Recording -> viewModel.stopRecording()
                    VoiceRecordingState.Processing -> {} // Do nothing during processing
                    VoiceRecordingState.Success -> viewModel.reset()
                    VoiceRecordingState.Error -> viewModel.reset()
                }
            },
            modifier = Modifier.size(80.dp),
            containerColor = when (uiState.recordingState) {
                VoiceRecordingState.Recording -> VoiceRecordingActive
                VoiceRecordingState.Processing -> VoiceRecordingProcessing
                VoiceRecordingState.Success -> VoiceRecordingSuccess
                else -> MaterialTheme.colorScheme.primary
            }
        ) {
            Icon(
                imageVector = if (uiState.recordingState == VoiceRecordingState.Recording) {
                    Icons.Default.MicOff
                } else {
                    Icons.Default.Mic
                },
                contentDescription = if (uiState.recordingState == VoiceRecordingState.Recording) {
                    "Parar gravação"
                } else {
                    "Iniciar gravação"
                },
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(40.dp)
            )
        }

        // Reset Button
        if (uiState.recordingState == VoiceRecordingState.Success || 
            uiState.recordingState == VoiceRecordingState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.reset() }
            ) {
                Text("Nova Gravação")
            }
        }
    }
}

@Composable
fun VoiceRecordingVisualizer(
    state: VoiceRecordingState,
    modifier: Modifier = Modifier
) {
    val animatedProgress = remember { Animatable(0f) }
    
    LaunchedEffect(state) {
        when (state) {
            VoiceRecordingState.Recording, VoiceRecordingState.Processing -> {
                animatedProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
            else -> {
                animatedProgress.snapTo(0f)
            }
        }
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(
                when (state) {
                    VoiceRecordingState.Idle -> VoiceRecordingIdle
                    VoiceRecordingState.Listening -> MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    VoiceRecordingState.Recording -> VoiceRecordingActive.copy(alpha = 0.3f)
                    VoiceRecordingState.Processing -> VoiceRecordingProcessing.copy(alpha = 0.3f)
                    VoiceRecordingState.Success -> VoiceRecordingSuccess.copy(alpha = 0.3f)
                    VoiceRecordingState.Error -> MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawVoiceWaveform(
                state = state,
                progress = animatedProgress.value
            )
        }
        
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = null,
            tint = when (state) {
                VoiceRecordingState.Idle -> VoiceRecordingIdle
                VoiceRecordingState.Listening -> MaterialTheme.colorScheme.primary
                VoiceRecordingState.Recording -> VoiceRecordingActive
                VoiceRecordingState.Processing -> VoiceRecordingProcessing
                VoiceRecordingState.Success -> VoiceRecordingSuccess
                VoiceRecordingState.Error -> MaterialTheme.colorScheme.error
            },
            modifier = Modifier.size(48.dp)
        )
    }
}

private fun DrawScope.drawVoiceWaveform(
    state: VoiceRecordingState,
    progress: Float
) {
    if (state == VoiceRecordingState.Recording || state == VoiceRecordingState.Processing) {
        val radius = size.minDimension / 2
        val strokeWidth = 8.dp.toPx()
        
        // Draw animated circles
        repeat(3) { index ->
            val animatedRadius = radius * 0.6f + (radius * 0.4f * progress * (index + 1) / 3)
            val alpha = 1f - (progress * (index + 1) / 3)
            
            drawCircle(
                color = when (state) {
                    VoiceRecordingState.Recording -> VoiceRecordingActive
                    VoiceRecordingState.Processing -> VoiceRecordingProcessing
                    else -> VoiceRecordingIdle
                }.copy(alpha = alpha * 0.6f),
                radius = animatedRadius,
                style = Stroke(width = strokeWidth)
            )
        }
    }
}