package com.guardem.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.guardem.app.ui.screens.LoginScreenSimple
import com.guardem.app.ui.screens.VoiceCaptureScreenMinimal
import com.guardem.app.ui.screens.MemoriesScreen
import com.guardem.app.ui.screens.SettingsScreen

@Composable
fun GuardeMeNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = GuardeMeDestinations.Login,
        modifier = modifier
    ) {
        composable(GuardeMeDestinations.Login) {
            LoginScreenSimple(
                onNavigateToVoice = {
                    navController.navigate(GuardeMeDestinations.VoiceCapture) {
                        popUpTo(GuardeMeDestinations.Login) { 
                            inclusive = true 
                        }
                    }
                }
            )
        }
        
        composable(GuardeMeDestinations.VoiceCapture) {
            VoiceCaptureScreenMinimal(
                onNavigateToMemories = {
                    navController.navigate(GuardeMeDestinations.Memories)
                },
                onNavigateToSettings = {
                    navController.navigate(GuardeMeDestinations.Settings)
                }
            )
        }
        
        composable(GuardeMeDestinations.Memories) {
            MemoriesScreen(
                onNavigateToVoice = {
                    navController.navigate(GuardeMeDestinations.VoiceCapture)
                }
            )
        }
        
        composable(GuardeMeDestinations.Settings) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.navigate(GuardeMeDestinations.Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

object GuardeMeDestinations {
    const val Login = "login"
    const val VoiceCapture = "voice_capture"
    const val Memories = "memories"
    const val Settings = "settings"
}