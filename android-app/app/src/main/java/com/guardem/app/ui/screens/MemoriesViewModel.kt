package com.guardem.app.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardem.app.data.remote.NetworkClient
import com.guardem.app.data.remote.dto.MemoryDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MemoriesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MemoriesUiState())
    val uiState: StateFlow<MemoriesUiState> = _uiState.asStateFlow()

    private val demoUserId = "d9a31a61-0376-4c0c-a037-692be019c6a1" // Real demo user ID

    init {
        loadMemories()
    }

    fun loadMemories() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val response = NetworkClient.apiService.getMemories(
                    userId = demoUserId,
                    limit = 50
                )

                if (response.isSuccessful) {
                    val memoriesResponse = response.body()
                    if (memoriesResponse != null) {
                        _uiState.value = _uiState.value.copy(
                            memories = memoriesResponse.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Erro ao processar resposta da API"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Erro ao carregar memórias: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erro de conexão: ${e.message}"
                )
            }
        }
    }

    fun refreshMemories() {
        loadMemories()
    }
}

data class MemoriesUiState(
    val memories: List<MemoryDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)