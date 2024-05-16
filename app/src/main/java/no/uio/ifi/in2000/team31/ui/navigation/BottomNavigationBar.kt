package no.uio.ifi.in2000.team31.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddReaction
import androidx.compose.material.icons.outlined.Hiking
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        val homeSelected = navController.currentDestination?.route == AppRoutes.HOME
        // hjemskjerm
        NavigationBarItem(
            // m3 default ikon og tekst
            icon = {
                Icon(
                if (homeSelected) Icons.Filled.Home else Icons.Outlined.Home,
                contentDescription = "Hjem"
                )
            },
            label = { Text("Hjem") },
            selected = homeSelected,
            onClick = {
                // hvis ikke vi er der allerede
                if (navController.currentDestination?.route != AppRoutes.HOME) {
                    navController.navigate(AppRoutes.HOME) {
                        // fikser backstack
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }

                        //unngår flere instanser
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )

        val moodSelected = navController.currentDestination?.route == AppRoutes.MOOD
        // humør
        NavigationBarItem(

            icon = {
                Icon(
                    if (moodSelected) Icons.Filled.AddReaction else Icons.Outlined.AddReaction,
                    contentDescription = "Humør"
                )
            },
            label = { Text("Humør") },
            selected = moodSelected,

            onClick = {
                if (navController.currentDestination?.route != AppRoutes.MOOD) {

                    // dra dit
                    navController.navigate(AppRoutes.MOOD) {
                        // fikser backstack
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }

                        //unngår flere instanser
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        val activitySelected = navController.currentDestination?.route == AppRoutes.ACTIVITY
        NavigationBarItem(

            // m3 default ikon og tekst
            icon = {
                Icon(
                    if (activitySelected) Icons.Filled.Hiking else Icons.Outlined.Hiking,
                    contentDescription = "Aktiviteter"
                )
            },
            label = { Text("Aktiviteter") },
            selected = activitySelected,
            onClick = {
                // hvis ikke vi er der allerede
                if (navController.currentDestination?.route != AppRoutes.ACTIVITY) {
                    navController.navigate(AppRoutes.ACTIVITY) {
                        // fikser backstack
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }

                        //unngår flere instanser
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        )
        val settingsSelected = navController.currentDestination?.route == AppRoutes.SETTINGS
        NavigationBarItem(
            selected = settingsSelected,
            onClick = {
                if (navController.currentDestination?.route != AppRoutes.SETTINGS) {
                    navController.navigate(AppRoutes.SETTINGS) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                } },
            label = { Text("Innstillinger") },
            icon = {
                Icon(
                    if (settingsSelected) Icons.Filled.Settings else Icons.Outlined.Settings,
                    contentDescription = "Settings"
                )
            }
        )
    }
}