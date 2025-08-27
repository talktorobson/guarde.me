package com.guardem.app.ui.screens.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val supabaseAuth: Auth
) : ViewModel() {

    private val prefs: SharedPreferences = context.getSharedPreferences("guarde_me_settings", Context.MODE_PRIVATE)
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
        observeAuthState()
    }

    private fun loadSettings() {
        _uiState.value = _uiState.value.copy(
            wakeWordEnabled = prefs.getBoolean("wake_word_enabled", true),
            microphoneSensitivity = prefs.getFloat("microphone_sensitivity", 0.7f),
            pushNotificationsEnabled = prefs.getBoolean("push_notifications_enabled", true),
            defaultChannel = prefs.getString("default_channel", "push") ?: "push",
            privacyModeEnabled = prefs.getBoolean("privacy_mode_enabled", false),
            totalMemories = prefs.getInt("total_memories", 0),
            lastSyncTime = prefs.getString("last_sync_time", null)
        )
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            supabaseAuth.sessionStatus.collect { status ->
                when (status) {
                    is io.github.jan.supabase.auth.status.SessionStatus.Authenticated -> {
                        _uiState.value = _uiState.value.copy(user = status.session.user)
                    }
                    else -> {
                        _uiState.value = _uiState.value.copy(user = null)
                    }
                }
            }
        }
    }

    fun setWakeWordEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(wakeWordEnabled = enabled)
        prefs.edit().putBoolean("wake_word_enabled", enabled).apply()
        Timber.d("Wake word enabled: $enabled")
    }

    fun setMicrophoneSensitivity(sensitivity: Float) {
        _uiState.value = _uiState.value.copy(microphoneSensitivity = sensitivity)
        prefs.edit().putFloat("microphone_sensitivity", sensitivity).apply()
        Timber.d("Microphone sensitivity: $sensitivity")
    }

    fun setPushNotificationsEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(pushNotificationsEnabled = enabled)
        prefs.edit().putBoolean("push_notifications_enabled", enabled).apply()
        Timber.d("Push notifications enabled: $enabled")
        
        // TODO: Register/unregister FCM token based on this setting
    }

    fun setDefaultChannel(channel: String) {
        _uiState.value = _uiState.value.copy(defaultChannel = channel)
        prefs.edit().putString("default_channel", channel).apply()
        Timber.d("Default channel: $channel")
    }

    fun setPrivacyModeEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(privacyModeEnabled = enabled)
        prefs.edit().putBoolean("privacy_mode_enabled", enabled).apply()
        Timber.d("Privacy mode enabled: $enabled")
    }

    fun clearLocalData() {
        viewModelScope.launch {
            try {
                // Clear shared preferences (keep auth settings)
                val authPrefs = mapOf(
                    "wake_word_enabled" to _uiState.value.wakeWordEnabled,
                    "push_notifications_enabled" to _uiState.value.pushNotificationsEnabled,
                    "default_channel" to _uiState.value.defaultChannel
                )
                
                prefs.edit().clear().apply()
                
                // Restore important settings
                with(prefs.edit()) {
                    putBoolean("wake_word_enabled", authPrefs["wake_word_enabled"] as Boolean)
                    putBoolean("push_notifications_enabled", authPrefs["push_notifications_enabled"] as Boolean)
                    putString("default_channel", authPrefs["default_channel"] as String)
                    apply()
                }
                
                // Clear app cache
                context.cacheDir.deleteRecursively()
                
                // Update last sync time
                updateLastSyncTime()
                
                Timber.d("Local data cleared successfully")
                
            } catch (e: Exception) {
                Timber.e(e, "Failed to clear local data")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                supabaseAuth.signOut()
                clearLocalData()
                Timber.d("User signed out")
            } catch (e: Exception) {
                Timber.e(e, "Failed to sign out")
            }
        }
    }

    fun updateMemoryCount(count: Int) {
        _uiState.value = _uiState.value.copy(totalMemories = count)
        prefs.edit().putInt("total_memories", count).apply()
    }

    private fun updateLastSyncTime() {
        val currentTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
        _uiState.value = _uiState.value.copy(lastSyncTime = currentTime)
        prefs.edit().putString("last_sync_time", currentTime).apply()
    }

    // Getters for other components to access settings
    fun getWakeWordEnabled(): Boolean = _uiState.value.wakeWordEnabled
    fun getMicrophoneSensitivity(): Float = _uiState.value.microphoneSensitivity
    fun getPushNotificationsEnabled(): Boolean = _uiState.value.pushNotificationsEnabled
    fun getDefaultChannel(): String = _uiState.value.defaultChannel
    fun getPrivacyModeEnabled(): Boolean = _uiState.value.privacyModeEnabled
}

data class SettingsUiState(
    val user: UserInfo? = null,
    val wakeWordEnabled: Boolean = true,
    val microphoneSensitivity: Float = 0.7f,
    val pushNotificationsEnabled: Boolean = true,
    val defaultChannel: String = "push",
    val privacyModeEnabled: Boolean = false,
    val totalMemories: Int = 0,
    val lastSyncTime: String? = null
)