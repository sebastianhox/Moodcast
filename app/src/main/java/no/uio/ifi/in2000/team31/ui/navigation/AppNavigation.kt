package no.uio.ifi.in2000.team31.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.team31.ui.alert.AlertScreen
import no.uio.ifi.in2000.team31.ui.home.HomeScreen
import no.uio.ifi.in2000.team31.ui.home.HomeViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {

        NavHost(navController = navController, startDestination = AppRoutes.HOME) {
            composable(AppRoutes.HOME) {
                val homeViewModel: HomeViewModel = viewModel()
                HomeScreen(homeViewModel, navController)
            }

            composable(AppRoutes.ALERT) {
                AlertScreen(navController)
            }
        }
    }
}

