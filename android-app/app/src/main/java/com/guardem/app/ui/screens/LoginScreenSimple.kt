package com.guardem.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreenSimple(
    onNavigateToVoice: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Guarde.me",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2E5BBA)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Simple Login Test",
            fontSize = 18.sp,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Ultra simple button with no custom styling
        Button(
            onClick = {
                println("BUTTON CLICKED!") // Debug log
                onNavigateToVoice()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("CLICK TO NAVIGATE")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Second test button
        OutlinedButton(
            onClick = {
                println("OUTLINED BUTTON CLICKED!") // Debug log
                onNavigateToVoice()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ALTERNATE BUTTON")
        }
    }
}