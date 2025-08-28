package com.guardem.app.ui.screens.memories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guardem.app.data.remote.api.GuardeMeApiService
import com.guardem.app.data.remote.dto.MemoryDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MemoriesViewModel @Inject constructor(
    private val apiService: GuardeMeApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(MemoriesUiState())
    val uiState: StateFlow<MemoriesUiState> = _uiState.asStateFlow()

    init {
        loadMemories()
    }

    fun loadMemories() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                
                // For now, use mock data until we implement user-specific memory retrieval
                val mockMemories = generateMockMemories()
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    memories = mockMemories,
                    filteredMemories = mockMemories
                )
                
            } catch (e: Exception) {
                Timber.e(e, "Failed to load memories")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erro ao carregar memórias: ${e.message}"
                )
            }
        }
    }

    fun searchMemories(query: String) {
        val filteredMemories = if (query.isBlank()) {
            _uiState.value.memories
        } else {
            _uiState.value.memories.filter { memory ->
                memory.contentText.contains(query, ignoreCase = true) ||
                memory.tags.any { tag -> tag.contains(query, ignoreCase = true) }
            }
        }
        
        _uiState.value = _uiState.value.copy(
            filteredMemories = filteredMemories,
            searchQuery = query
        )
    }

    fun selectMemory(memory: MemoryWithSchedule) {
        _uiState.value = _uiState.value.copy(selectedMemory = memory)
        Timber.d("Memory selected: ${memory.id}")
    }

    fun refreshMemories() {
        loadMemories()
    }

    private fun generateMockMemories(): List<MemoryWithSchedule> {
        return listOf(
            MemoryWithSchedule(
                id = "1",
                userId = "test-user",
                contentText = "Lembrar de comprar leite no supermercado",
                contentType = "text",
                tags = listOf("compras", "alimentação"),
                createdAt = "2025-08-27T10:30:00.000000Z",
                hasSchedule = true,
                isDelivered = false,
                nextRunAt = "2025-08-28T09:00:00Z",
                scheduleType = "datetime",
                channel = "push"
            ),
            MemoryWithSchedule(
                id = "2",
                userId = "test-user",
                contentText = "Ligar para a mamãe no aniversário dela",
                contentType = "text",
                tags = listOf("família", "aniversário"),
                createdAt = "2025-08-26T15:45:00.000000Z",
                hasSchedule = true,
                isDelivered = false,
                nextRunAt = "2025-12-15T10:00:00Z",
                scheduleType = "date",
                channel = "push"
            ),
            MemoryWithSchedule(
                id = "3",
                userId = "test-user",
                contentText = "Revisar apresentação do projeto",
                contentType = "text",
                tags = listOf("trabalho", "projeto"),
                createdAt = "2025-08-25T08:20:00.000000Z",
                hasSchedule = false,
                isDelivered = true,
                nextRunAt = null,
                scheduleType = null,
                channel = "push"
            ),
            MemoryWithSchedule(
                id = "4",
                userId = "test-user",
                contentText = "Fazer exercícios todas as segundas às 7h",
                contentType = "text",
                tags = listOf("saúde", "exercícios", "rotina"),
                createdAt = "2025-08-24T19:10:00.000000Z",
                hasSchedule = true,
                isDelivered = false,
                nextRunAt = "2025-09-02T07:00:00Z",
                scheduleType = "recurrence",
                channel = "push"
            ),
            MemoryWithSchedule(
                id = "5",
                userId = "test-user",
                contentText = "Estudar português para o exame",
                contentType = "text",
                tags = listOf("estudos", "idiomas"),
                createdAt = "2025-08-23T12:00:00.000000Z",
                hasSchedule = false,
                isDelivered = false,
                nextRunAt = null,
                scheduleType = null,
                channel = "push"
            )
        )
    }
}

data class MemoriesUiState(
    val isLoading: Boolean = false,
    val memories: List<MemoryWithSchedule> = emptyList(),
    val filteredMemories: List<MemoryWithSchedule> = emptyList(),
    val selectedMemory: MemoryWithSchedule? = null,
    val searchQuery: String = "",
    val errorMessage: String? = null
)

data class MemoryWithSchedule(
    val id: String,
    val userId: String,
    val contentText: String,
    val contentType: String,
    val tags: List<String>,
    val createdAt: String,
    val hasSchedule: Boolean,
    val isDelivered: Boolean,
    val nextRunAt: String?,
    val scheduleType: String?,
    val channel: String
)