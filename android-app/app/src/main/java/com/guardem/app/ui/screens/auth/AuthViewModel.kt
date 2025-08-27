package com.guardem.app.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val supabaseAuth: Auth
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            supabaseAuth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = true,
                            isLoading = false,
                            user = status.session.user,
                            errorMessage = null
                        )
                        Timber.d("User authenticated: ${status.session.user?.email}")
                    }
                    is SessionStatus.LoadingFromStorage -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true
                        )
                    }
                    is SessionStatus.NetworkError -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Erro de conexão. Verifique sua internet."
                        )
                        Timber.e("Auth network error: ${status.message}")
                    }
                    is SessionStatus.NotAuthenticated -> {
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = false,
                            isLoading = false,
                            user = null,
                            errorMessage = null
                        )
                        Timber.d("User not authenticated")
                    }
                }
            }
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

                supabaseAuth.signInWith(Google)
                
                // Success is handled by the session status observer
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao fazer login: ${e.message}"
                )
                Timber.e(e, "Google sign in failed")
            }
        }
    }

    fun continueAsGuest() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    errorMessage = null
                )

                // Create anonymous user
                supabaseAuth.signInAnonymously()
                
                // Success is handled by the session status observer
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao continuar como visitante: ${e.message}"
                )
                Timber.e(e, "Anonymous sign in failed")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                supabaseAuth.signOut()
            } catch (e: Exception) {
                Timber.e(e, "Sign out failed")
            }
        }
    }
}

data class AuthUiState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val user: io.github.jan.supabase.auth.user.UserInfo? = null,
    val errorMessage: String? = null
)