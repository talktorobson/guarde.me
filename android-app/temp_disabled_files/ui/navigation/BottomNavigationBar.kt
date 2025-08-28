package com.guardem.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Pop up to the start destination to avoid building up a large stack
                            popUpTo(GuardeMeDestinations.VoiceCapture) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

private val bottomNavItems = listOf(
    BottomNavItem(
        route = GuardeMeDestinations.Memories,
        icon = Icons.Default.History,
        label = "Memórias"
    ),
    BottomNavItem(
        route = GuardeMeDestinations.VoiceCapture,
        icon = Icons.Default.Mic,
        label = "Gravar"
    ),
    BottomNavItem(
        route = GuardeMeDestinations.Settings,
        icon = Icons.Default.Settings,
        label = "Config"
    )
)

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)