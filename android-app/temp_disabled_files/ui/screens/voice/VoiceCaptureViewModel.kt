package com.guardem.app.ui.screens.voice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardem.app.data.remote.api.GuardeMeApiService
import com.guardem.app.data.remote.dto.IntentDecodeRequest
import com.guardem.app.data.remote.dto.IntentDecodeResponse
import com.guardem.app.data.remote.dto.ScheduleCreateRequest
import com.guardem.app.data.remote.dto.MemoryCreateRequest
import com.guardem.app.data.remote.dto.ScheduleCreateRequestDetails
import com.guardem.app.data.speech.SpeechRecognitionService
import com.guardem.app.data.speech.SpeechRecognitionEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoiceCaptureViewModel @Inject constructor(
    private val apiService: GuardeMeApiService,
    private val speechService: SpeechRecognitionService
) : ViewModel() {

    private val _uiState = MutableStateFlow(VoiceCaptureUiState())
    val uiState: StateFlow<VoiceCaptureUiState> = _uiState.asStateFlow()

    private var speechJob: Job? = null

    fun startListening() {
        if (!speechService.isRecognitionAvailable()) {
            _uiState.value = _uiState.value.copy(
                recordingState = VoiceRecordingState.Error,
                errorMessage = "Reconhecimento de voz não disponível"
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            recordingState = VoiceRecordingState.Listening,
            transcript = "",
            intentResult = null,
            errorMessage = null
        )

        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            speechService.startWakeWordDetection()
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

    fun stopListening() {
        speechJob?.cancel()
        speechService.stopRecognition()
        _uiState.value = _uiState.value.copy(
            recordingState = VoiceRecordingState.Idle
        )
    }

    fun startRecording() {
        _uiState.value = _uiState.value.copy(
            recordingState = VoiceRecordingState.Recording,
            transcript = ""
        )

        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            speechService.startMemoryRecording()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        recordingState = VoiceRecordingState.Error,
                        errorMessage = "Erro na gravação: ${e.message}"
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
        _uiState.value = _uiState.value.copy(
            recordingState = VoiceRecordingState.Processing
        )
    }

    private fun handleSpeechEvent(event: SpeechRecognitionEvent) {
        when (event) {
            is SpeechRecognitionEvent.WakeWordDetected -> {
                startRecording()
            }
            is SpeechRecognitionEvent.PartialResult -> {
                _uiState.value = _uiState.value.copy(
                    transcript = event.text
                )
            }
            is SpeechRecognitionEvent.SpeechResult -> {
                _uiState.value = _uiState.value.copy(
                    transcript = event.text
                )
                processTranscript(event.text)
            }
            is SpeechRecognitionEvent.Error -> {
                _uiState.value = _uiState.value.copy(
                    recordingState = VoiceRecordingState.Error,
                    errorMessage = event.message
                )
            }
            is SpeechRecognitionEvent.BeginningOfSpeech -> {
                // Optional: Could show visual feedback
            }
            is SpeechRecognitionEvent.EndOfSpeech -> {
                // Speech ended, wait for results
            }
            is SpeechRecognitionEvent.ReadyForSpeech -> {
                // Ready to listen
            }
            is SpeechRecognitionEvent.RmsChanged -> {
                // Optional: Could use for audio level visualization
            }
        }
    }

    private fun processTranscript(transcript: String) {
        viewModelScope.launch {
            try {
                val request = IntentDecodeRequest(
                    transcriptRedacted = transcript,
                    partial = false
                )
                
                val response = apiService.decodeIntent(request)
                
                if (response.isSuccessful) {
                    val intentResult = response.body()
                    if (intentResult != null && intentResult.intent == "SAVE_MEMORY") {
                        createMemorySchedule(transcript, intentResult)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            recordingState = VoiceRecordingState.Success,
                            intentResult = intentResult
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        recordingState = VoiceRecordingState.Error,
                        errorMessage = "Erro na análise da IA: ${response.code()}"
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

    private fun createMemorySchedule(content: String, intentResult: IntentDecodeResponse) {
        viewModelScope.launch {
            try {
                val request = ScheduleCreateRequest(
                    userId = "temp-user-id", // TODO: Get from auth
                    memory = MemoryCreateRequest(
                        contentType = intentResult.slots.contentType ?: "text",
                        contentText = content
                    ),
                    schedule = ScheduleCreateRequestDetails(
                        whenType = intentResult.slots.whenType ?: "datetime",
                        dtstart = intentResult.slots.whenValue,
                        channel = intentResult.slots.channel ?: "push"
                    )
                )
                
                val response = apiService.createSchedule(request)
                
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        recordingState = VoiceRecordingState.Success,
                        intentResult = intentResult
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        recordingState = VoiceRecordingState.Error,
                        errorMessage = "Erro ao salvar memória: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    recordingState = VoiceRecordingState.Error,
                    errorMessage = "Erro ao salvar: ${e.message}"
                )
            }
        }
    }

    fun reset() {
        speechJob?.cancel()
        speechService.stopRecognition()
        _uiState.value = VoiceCaptureUiState()
    }

    override fun onCleared() {
        super.onCleared()
        speechJob?.cancel()
        speechService.stopRecognition()
    }
}

data class VoiceCaptureUiState(
    val recordingState: VoiceRecordingState = VoiceRecordingState.Idle,
    val transcript: String = "",
    val intentResult: IntentDecodeResponse? = null,
    val errorMessage: String? = null
)

enum class VoiceRecordingState {
    Idle,
    Listening,
    Recording,
    Processing,
    Success,
    Error
}