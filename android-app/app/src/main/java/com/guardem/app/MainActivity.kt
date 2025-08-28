package com.guardem.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.guardem.app.ui.navigation.BottomNavigationBar
import com.guardem.app.ui.navigation.GuardeMeDestinations
import com.guardem.app.ui.navigation.GuardeMeNavigation
import com.guardem.app.ui.theme.GuardeMeTheme
// import dagger.hilt.android.AndroidEntryPoint

// @AndroidEntryPoint  // Temporarily disabled for build fix
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            GuardeMeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
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

        // Show bottom navigation only for main app screens (not login)
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
}