package com.guardem.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.guardem.app.ui.theme.*

@Composable
fun VoiceCaptureScreenMinimal(
    onNavigateToMemories: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val design = LocalGuardeMeDesign
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(design.spacing.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Simple header
        Text(
            text = "🎙️ Voice Capture",
            style = design.typography.displayMedium,
            color = design.colors.primary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(design.spacing.lg))
        
        Text(
            text = "Navigation Working!",
            style = design.typography.headlineMedium,
            color = design.colors.onBackground,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(design.spacing.xl))
        
        // Simple navigation buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(design.spacing.md)
        ) {
            Button(
                onClick = onNavigateToMemories,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = design.colors.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    tint = design.colors.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Memories",
                    color = design.colors.onPrimary
                )
            }
            
            OutlinedButton(
                onClick = onNavigateToSettings
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Settings")
            }
        }
    }
}