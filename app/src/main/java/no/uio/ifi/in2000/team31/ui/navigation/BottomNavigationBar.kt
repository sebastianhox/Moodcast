package no.uio.ifi.in2000.team31.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {


        // hjemskjerm
        NavigationBarItem(

            // m3 default ikon og tekst
            icon = { Icon(Icons.Filled.Home, contentDescription = "Hjem") },
            label = { Text("Hjem") },

            selected = navController.currentDestination?.route == AppRoutes.HOME,
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

        // farevarsel
        NavigationBarItem(

            icon = { Icon(Icons.Filled.Warning, contentDescription = "Varsler") },
            label = { Text("Varsler") },
            selected = navController.currentDestination?.route == AppRoutes.ALERT,

            onClick = {

                // hvis vi ikke er på alert allerede
                if (navController.currentDestination?.route != AppRoutes.ALERT) {

                    // dra dit
                    navController.navigate(AppRoutes.ALERT) {
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
        // humør
        NavigationBarItem(

            icon = { Icon(Icons.Filled.Warning, contentDescription = "mood") },
            label = { Text("Humør") },
            selected = navController.currentDestination?.route == AppRoutes.MOOD,

            onClick = {

                // hvis vi ikke er på alert allerede
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

        // aktiviteter
    }
}




