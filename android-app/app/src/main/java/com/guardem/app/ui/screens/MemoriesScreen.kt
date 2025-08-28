package com.guardem.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MemoriesScreen(
    onNavigateToVoice: () -> Unit
) {
    val viewModel: MemoriesViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    
    // Date formatter for displaying creation dates
    val dateFormatter = remember {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Suas Memórias",
                    color = MaterialTheme.colors.onPrimary
                )
            },
            actions = {
                IconButton(onClick = { viewModel.refreshMemories() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Atualizar",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            },
            backgroundColor = MaterialTheme.colors.primary
        )
        
        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colors.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Carregando memórias...",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
                    )
                }
            }
            
            uiState.errorMessage != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "❌ ${uiState.errorMessage}",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.refreshMemories() }) {
                        Text("Tentar Novamente")
                    }
                }
            }
            
            uiState.memories.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Nenhuma memória ainda",
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.6f)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Comece gravando sua primeira memória",
                        style = MaterialTheme.typography.body2,
                        color = MaterialTheme.colors.onBackground.copy(alpha = 0.4f)
                    )
                }
            }
            
            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.memories) { memory ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = memory.contentText ?: "Conteúdo não disponível",
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.onSurface
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                // Display tags if available
                                if (memory.tags.isNotEmpty()) {
                                    Text(
                                        text = "🏷️ ${memory.tags.joinToString(", ")}",
                                        style = MaterialTheme.typography.caption,
                                        color = MaterialTheme.colors.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                                
                                // Format and display creation date
                                val createdDate = try {
                                    val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
                                    val date = isoFormatter.parse(memory.createdAt)
                                    dateFormatter.format(date ?: Date())
                                } catch (e: Exception) {
                                    "Data indisponível"
                                }
                                
                                Text(
                                    text = "$createdDate • ${memory.source?.uppercase() ?: "ORIGEM DESCONHECIDA"}",
                                    style = MaterialTheme.typography.caption,
                                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Floating Action Button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = onNavigateToVoice,
                modifier = Modifier.padding(16.dp),
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nova memória",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}