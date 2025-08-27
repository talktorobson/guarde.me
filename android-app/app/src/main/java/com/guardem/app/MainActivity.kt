package com.guardem.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.guardem.app.ui.navigation.BottomNavigationBar
import com.guardem.app.ui.navigation.GuardeMeDestinations
import com.guardem.app.ui.navigation.GuardeMeNavigation
import com.guardem.app.ui.theme.GuardeMeTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Permission launcher for audio recording
    private val audioPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Timber.d("Audio permission granted")
        } else {
            Timber.w("Audio permission denied")
        }
    }

    // Permission launcher for notifications (Android 13+)
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Timber.d("Notification permission granted")
        } else {
            Timber.w("Notification permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            GuardeMeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GuardeMeApp()
                }
            }
        }
    }

    @Composable
    fun GuardeMeApp() {
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route

        // Request permissions on app start
        LaunchedEffect(Unit) {
            requestNecessaryPermissions()
        }

        // Show bottom navigation only for main app screens
        val showBottomNav = currentRoute in listOf(
            GuardeMeDestinations.VoiceCapture,
            GuardeMeDestinations.Memories,
            GuardeMeDestinations.Settings
        )

        if (showBottomNav) {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(navController = navController)
                }
            ) { innerPadding ->
                GuardeMeNavigation(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        } else {
            GuardeMeNavigation(navController = navController)
        }
    }

    private fun requestNecessaryPermissions() {
        // Request audio recording permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

        // Request notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}