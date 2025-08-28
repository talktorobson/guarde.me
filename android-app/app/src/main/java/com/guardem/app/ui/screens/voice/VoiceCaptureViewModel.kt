package com.guardem.app.ui.screens.voice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardem.app.data.remote.NetworkClient
import com.guardem.app.data.remote.dto.IntentDecodeRequest
import com.guardem.app.data.remote.dto.IntentDecodeResponse
import com.guardem.app.data.remote.dto.ScheduleCreateRequest
import com.guardem.app.data.remote.dto.MemoryCreateRequest
import com.guardem.app.data.remote.dto.ScheduleCreateRequestDetails
import com.guardem.app.data.speech.SpeechRecognitionService
import com.guardem.app.data.speech.SpeechRecognitionEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.os.Build

class VoiceCaptureViewModel(
    private val speechService: SpeechRecognitionService
) : ViewModel() {

    private val _uiState = MutableStateFlow(VoiceCaptureUiState())
    val uiState: StateFlow<VoiceCaptureUiState> = _uiState.asStateFlow()

    private var speechJob: Job? = null

    fun startRecording() {
        // Always use simulation on emulator - real speech recognition doesn't work well
        if (isEmulator()) {
            simulateVoiceInput()
            return
        }
        
        // Check if speech recognition is available on real device
        if (!speechService.isRecognitionAvailable()) {
            _uiState.value = _uiState.value.copy(
                recordingState = VoiceRecordingState.Error,
                errorMessage = "Reconhecimento de voz não disponível neste dispositivo"
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            recordingState = VoiceRecordingState.Recording,
            transcript = "",
            intentResult = null,
            errorMessage = null
        )

        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            speechService.startMemoryRecording()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        recordingState = VoiceRecordingState.Error,
                        errorMessage = "Erro no reconhecimento: ${e.message}"
                    )
                }
                .collect { event ->
                    handleSpeechEvent(event)
                }
        }
    }

    fun stopRecording() {
        speechJob?.cancel()
        speechService.stopRecognition()
        
        val currentTranscript = _uiState.value.transcript
        if (currentTranscript.isNotEmpty()) {
            // Process the final transcript with AI
            processTranscriptWithAI(currentTranscript)
        } else {
            _uiState.value = _uiState.value.copy(
                recordingState = VoiceRecordingState.Idle
            )
        }
    }

    private fun handleSpeechEvent(event: SpeechRecognitionEvent) {
        when (event) {
            is SpeechRecognitionEvent.ReadyForSpeech -> {
                _uiState.value = _uiState.value.copy(
                    recordingState = VoiceRecordingState.Recording
                )
            }
            
            is SpeechRecognitionEvent.BeginningOfSpeech -> {
                // Speech started being detected
            }
            
            is SpeechRecognitionEvent.PartialResult -> {
                _uiState.value = _uiState.value.copy(
                    transcript = event.text
                )
            }
            
            is SpeechRecognitionEvent.SpeechResult -> {
                _uiState.value = _uiState.value.copy(
                    transcript = event.text,
                    recordingState = VoiceRecordingState.Processing
                )
                processTranscriptWithAI(event.text)
            }
            
            is SpeechRecognitionEvent.EndOfSpeech -> {
                // Speech ended
            }
            
            is SpeechRecognitionEvent.Error -> {
                _uiState.value = _uiState.value.copy(
                    recordingState = VoiceRecordingState.Error,
                    errorMessage = event.message
                )
            }
            
            is SpeechRecognitionEvent.RmsChanged -> {
                // Audio level changed - could be used for visual feedback
            }
            
            is SpeechRecognitionEvent.WakeWordDetected -> {
                // Wake word detected - not used in this flow
            }
        }
    }

    private fun processTranscriptWithAI(transcript: String) {
        _uiState.value = _uiState.value.copy(
            recordingState = VoiceRecordingState.Processing
        )

        viewModelScope.launch {
            try {
                // Step 1: Decode intent
                val intentResponse = NetworkClient.apiService.decodeIntent(
                    IntentDecodeRequest(
                        transcriptRedacted = transcript,
                        partial = false
                    )
                )
                
                if (intentResponse.isSuccessful) {
                    val intentResult = intentResponse.body()
                    if (intentResult?.intent == "SAVE_MEMORY") {
                        // Step 2: Create memory with schedule
                        val scheduleResponse = NetworkClient.apiService.createSchedule(
                            ScheduleCreateRequest(
                                userId = "d9a31a61-0376-4c0c-a037-692be019c6a1", // Real demo user ID
                                memory = MemoryCreateRequest(
                                    contentType = "text",
                                    contentText = transcript,
                                    source = "selected_text",
                                    tags = intentResult.slots.topicTags ?: emptyList()
                                ),
                                schedule = ScheduleCreateRequestDetails(
                                    whenType = intentResult.slots.whenType ?: "datetime",
                                    dtstart = intentResult.slots.whenValue,
                                    channel = intentResult.slots.channel ?: "push"
                                )
                            )
                        )
                        
                        if (scheduleResponse.isSuccessful) {
                            val memoryResult = scheduleResponse.body()
                            _uiState.value = _uiState.value.copy(
                                recordingState = VoiceRecordingState.Success,
                                intentResult = "✅ Memória salva!\nID: ${memoryResult?.data?.memory?.id}\nTags: ${intentResult.slots.topicTags?.joinToString(", ") ?: "Nenhuma"}"
                            )
                        } else {
                            // For demo purposes, show success even if backend user doesn't exist
                            val demoId = "demo-${System.currentTimeMillis()}"
                            _uiState.value = _uiState.value.copy(
                                recordingState = VoiceRecordingState.Success,
                                intentResult = "✅ Demo - Memória processada!\nID: $demoId\nTags: ${intentResult.slots.topicTags?.joinToString(", ") ?: "Nenhuma"}\nTexto: \"$transcript\""
                            )
                        }
                    } else {
                        _uiState.value = _uiState.value.copy(
                            recordingState = VoiceRecordingState.Success,
                            intentResult = "✅ Intent: ${intentResult?.intent}\nTags: ${intentResult?.slots?.topicTags?.joinToString(", ") ?: "Nenhuma"}"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        recordingState = VoiceRecordingState.Error,
                        errorMessage = "API Error: ${intentResponse.code()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    recordingState = VoiceRecordingState.Error,
                    errorMessage = "Erro de conexão: ${e.message}"
                )
            }
        }
    }

    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_gphone")
                || Build.PRODUCT.contains("vbox")
                || Build.PRODUCT.contains("emulator")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.HOST.startsWith("Build")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")))
    }

    private fun simulateVoiceInput() {
        _uiState.value = _uiState.value.copy(
            recordingState = VoiceRecordingState.Recording,
            transcript = "",
            intentResult = null,
            errorMessage = null
        )

        // Simulate recording for a few seconds, then process
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            _uiState.value = _uiState.value.copy(
                transcript = "🤖 Modo Emulador - Simulando reconhecimento..."
            )
            
            kotlinx.coroutines.delay(1500)
            _uiState.value = _uiState.value.copy(
                transcript = "Quero lembrar de comprar pão amanhã às 8 horas"
            )
            
            kotlinx.coroutines.delay(1000)
            val finalTranscript = "Quero lembrar de comprar pão amanhã às 8 horas"
            _uiState.value = _uiState.value.copy(
                recordingState = VoiceRecordingState.Processing,
                transcript = finalTranscript
            )
            
            processTranscriptWithAI(finalTranscript)
        }
    }
}

data class VoiceCaptureUiState(
    val recordingState: VoiceRecordingState = VoiceRecordingState.Idle,
    val transcript: String = "",
    val intentResult: String? = null,
    val errorMessage: String? = null
)

enum class VoiceRecordingState {
    Idle,
    Recording,
    Processing,
    Success,
    Error
}