package no.uio.ifi.in2000.team31.ui.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.team31.ui.alert.AlertScreen
import no.uio.ifi.in2000.team31.ui.home.HomeScreen
import no.uio.ifi.in2000.team31.ui.home.HomeViewModel
import no.uio.ifi.in2000.team31.ui.mood.MoodScreen

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation(homeViewModel: HomeViewModel) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {

        NavHost(navController = navController, startDestination = AppRoutes.HOME) {
            composable(AppRoutes.HOME) {
                HomeScreen(navController, homeViewModel)
            }

            composable(AppRoutes.ALERT) {
                AlertScreen(navController)
            }

            composable(AppRoutes.MOOD) {
                MoodScreen(navController = navController)
            }
        }
    }
}

