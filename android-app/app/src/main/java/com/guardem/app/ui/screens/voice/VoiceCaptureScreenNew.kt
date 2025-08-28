package com.guardem.app.ui.screens.voice

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guardem.app.data.speech.SpeechRecognitionService

@Composable
fun VoiceCaptureScreenNew(
    onNavigateToMemories: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val context = LocalContext.current
    val speechService = remember { SpeechRecognitionService(context) }
    val viewModel: VoiceCaptureViewModel = viewModel { VoiceCaptureViewModel(speechService) }
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Status text based on recording state
        Text(
            text = when (uiState.recordingState) {
                VoiceRecordingState.Idle -> "Pronto para gravar"
                VoiceRecordingState.Recording -> "Ouvindo... fale agora!"
                VoiceRecordingState.Processing -> "🤖 Processando com IA..."
                VoiceRecordingState.Success -> "Processamento concluído!"
                VoiceRecordingState.Error -> "Erro no processamento"
            },
            style = MaterialTheme.typography.h5,
            color = when (uiState.recordingState) {
                VoiceRecordingState.Recording -> MaterialTheme.colors.error
                VoiceRecordingState.Processing -> MaterialTheme.colors.secondary
                VoiceRecordingState.Success -> MaterialTheme.colors.primary
                VoiceRecordingState.Error -> MaterialTheme.colors.error
                else -> MaterialTheme.colors.primary
            }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Recording button
        FloatingActionButton(
            onClick = {
                when (uiState.recordingState) {
                    VoiceRecordingState.Idle -> viewModel.startRecording()
                    VoiceRecordingState.Recording -> viewModel.stopRecording()
                    else -> { /* Do nothing during processing */ }
                }
            },
            modifier = Modifier.size(80.dp),
            backgroundColor = when (uiState.recordingState) {
                VoiceRecordingState.Recording -> MaterialTheme.colors.error
                VoiceRecordingState.Processing -> MaterialTheme.colors.secondary
                else -> MaterialTheme.colors.primary
            }
        ) {
            Icon(
                imageVector = if (uiState.recordingState == VoiceRecordingState.Recording) 
                    Icons.Default.Stop else Icons.Default.Mic,
                contentDescription = if (uiState.recordingState == VoiceRecordingState.Recording) 
                    "Parar gravação" else "Iniciar gravação",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colors.onPrimary
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Instruction text
        when (uiState.recordingState) {
            VoiceRecordingState.Recording -> {
                Text(
                    text = "Fale sobre sua memória...\nDiga 'Guarde me...' seguido do que deseja lembrar",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                )
            }
            VoiceRecordingState.Processing -> {
                Text(
                    text = "Processando sua fala com Inteligência Artificial...",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.secondary
                )
            }
            VoiceRecordingState.Idle -> {
                Text(
                    text = "Toque no microfone para começar a gravar",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                )
            }
            else -> { /* Other states handled by result cards */ }
        }
        
        // Real-time transcript display
        if (uiState.transcript.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.surface.copy(alpha = 0.9f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🎤 Reconhecimento em tempo real:",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.primary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "\"${uiState.transcript}\"",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
        
        // AI processing result
        if (uiState.intentResult != null) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp,
                backgroundColor = when (uiState.recordingState) {
                    VoiceRecordingState.Success -> MaterialTheme.colors.primary.copy(alpha = 0.1f)
                    else -> MaterialTheme.colors.error.copy(alpha = 0.1f)
                }
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "🧠 Resultado da IA:",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.secondary
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = uiState.intentResult ?: "",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
        
        // Error message
        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colors.error.copy(alpha = 0.1f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "⚠️ Erro:",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.error
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = uiState.errorMessage ?: "",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onNavigateToMemories,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.secondary
                )
            ) {
                Text(
                    text = "Ver Memórias",
                    color = MaterialTheme.colors.onSecondary
                )
            }
            
            Button(
                onClick = onNavigateToSettings,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                elevation = ButtonDefaults.elevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = "Configurações",
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}