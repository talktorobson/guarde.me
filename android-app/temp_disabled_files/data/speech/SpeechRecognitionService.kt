package com.guardem.app.data.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpeechRecognitionService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var speechRecognizer: SpeechRecognizer? = null
    private val wakeWord = "guarde me"

    fun startWakeWordDetection(): Flow<SpeechRecognitionEvent> = callbackFlow {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        
        val recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                trySend(SpeechRecognitionEvent.ReadyForSpeech)
            }

            override fun onBeginningOfSpeech() {
                trySend(SpeechRecognitionEvent.BeginningOfSpeech)
            }

            override fun onRmsChanged(rmsdB: Float) {
                trySend(SpeechRecognitionEvent.RmsChanged(rmsdB))
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Not used for our implementation
            }

            override fun onEndOfSpeech() {
                trySend(SpeechRecognitionEvent.EndOfSpeech)
            }

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Erro de áudio"
                    SpeechRecognizer.ERROR_CLIENT -> "Erro do cliente"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permissões insuficientes"
                    SpeechRecognizer.ERROR_NETWORK -> "Erro de rede"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Timeout de rede"
                    SpeechRecognizer.ERROR_NO_MATCH -> "Nenhuma correspondência encontrada"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconhecedor ocupado"
                    SpeechRecognizer.ERROR_SERVER -> "Erro do servidor"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Timeout de fala"
                    else -> "Erro desconhecido: $error"
                }
                trySend(SpeechRecognitionEvent.Error(errorMessage))
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val recognizedText = matches[0].lowercase(Locale.getDefault())
                    
                    if (recognizedText.contains(wakeWord)) {
                        trySend(SpeechRecognitionEvent.WakeWordDetected(recognizedText))
                    } else {
                        trySend(SpeechRecognitionEvent.SpeechResult(recognizedText))
                    }
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    trySend(SpeechRecognitionEvent.PartialResult(matches[0]))
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Not used for our implementation
            }
        }

        speechRecognizer?.setRecognitionListener(recognitionListener)
        
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "pt-BR")
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_VARIANCE, "pt-BR")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        
        speechRecognizer?.startListening(intent)

        awaitClose {
            stopRecognition()
        }
    }

    fun startMemoryRecording(): Flow<SpeechRecognitionEvent> = callbackFlow {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        
        val recognitionListener = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                trySend(SpeechRecognitionEvent.ReadyForSpeech)
            }

            override fun onBeginningOfSpeech() {
                trySend(SpeechRecognitionEvent.BeginningOfSpeech)
            }

            override fun onRmsChanged(rmsdB: Float) {
                trySend(SpeechRecognitionEvent.RmsChanged(rmsdB))
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // Not used for our implementation
            }

            override fun onEndOfSpeech() {
                trySend(SpeechRecognitionEvent.EndOfSpeech)
            }

            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    SpeechRecognizer.ERROR_AUDIO -> "Erro de áudio"
                    SpeechRecognizer.ERROR_CLIENT -> "Erro do cliente"
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permissões insuficientes"
                    SpeechRecognizer.ERROR_NETWORK -> "Erro de rede"
                    SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Timeout de rede"
                    SpeechRecognizer.ERROR_NO_MATCH -> "Nenhuma correspondência encontrada"
                    SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconhecedor ocupado"
                    SpeechRecognizer.ERROR_SERVER -> "Erro do servidor"
                    SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Timeout de fala"
                    else -> "Erro desconhecido: $error"
                }
                trySend(SpeechRecognitionEvent.Error(errorMessage))
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    trySend(SpeechRecognitionEvent.SpeechResult(matches[0]))
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    trySend(SpeechRecognitionEvent.PartialResult(matches[0]))
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // Not used for our implementation
            }
        }

        speechRecognizer?.setRecognitionListener(recognitionListener)
        
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "pt-BR")
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_VARIANCE, "pt-BR")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1000)
        }
        
        speechRecognizer?.startListening(intent)

        awaitClose {
            stopRecognition()
        }
    }

    fun stopRecognition() {
        speechRecognizer?.stopListening()
        speechRecognizer?.destroy()
        speechRecognizer = null
    }

    fun isRecognitionAvailable(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(context)
    }
}

sealed class SpeechRecognitionEvent {
    object ReadyForSpeech : SpeechRecognitionEvent()
    object BeginningOfSpeech : SpeechRecognitionEvent()
    object EndOfSpeech : SpeechRecognitionEvent()
    data class RmsChanged(val rmsdB: Float) : SpeechRecognitionEvent()
    data class PartialResult(val text: String) : SpeechRecognitionEvent()
    data class SpeechResult(val text: String) : SpeechRecognitionEvent()
    data class WakeWordDetected(val text: String) : SpeechRecognitionEvent()
    data class Error(val message: String) : SpeechRecognitionEvent()
}