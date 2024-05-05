package no.uio.ifi.in2000.team31.ui.navigation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.team31.ui.activity.ActivityScreen
import no.uio.ifi.in2000.team31.ui.alert.AlertScreen
import no.uio.ifi.in2000.team31.ui.home.HomeScreen
import no.uio.ifi.in2000.team31.ui.home.HomeViewModel
import no.uio.ifi.in2000.team31.ui.mood.MoodScreen
import no.uio.ifi.in2000.team31.ui.settings.SettingsScreen
import no.uio.ifi.in2000.team31.ui.settings.SettingsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation(homeViewModel: HomeViewModel, settingsViewModel: SettingsViewModel) {

    val navController = rememberNavController()
    //val homeViewModel: HomeViewModel = viewModel()

    NavHost(navController = navController, startDestination = AppRoutes.HOME) {
        composable(
            AppRoutes.HOME,
            enterTransition = {
                when (initialState.destination.route) {
                    AppRoutes.ALERT ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(700)
                        )

                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    AppRoutes.ALERT ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(700)
                        )

                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    AppRoutes.ALERT ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(700)
                        )

                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    AppRoutes.ALERT ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(700)
                        )

                    else -> null
                }
            }
        ) {
            HomeScreen(navController, homeViewModel)
        }

        composable(
            AppRoutes.ALERT,
            enterTransition = {
                when (initialState.destination.route) {
                    AppRoutes.HOME ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(700)
                        )

                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    AppRoutes.HOME ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(700)
                        )

                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    AppRoutes.HOME ->
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(700)
                        )

                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    AppRoutes.HOME ->
                        slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(700)
                        )

                    else -> null
                }

            }

        ) {
            AlertScreen(navController)
        }

        composable(AppRoutes.ACTIVITY) {
            ActivityScreen(navController)
        }

        composable(AppRoutes.MOOD) {
            MoodScreen(navController = navController)
        }

        composable(AppRoutes.SETTINGS) {
            SettingsScreen(navController, settingsViewModel)
        }
    }
}

