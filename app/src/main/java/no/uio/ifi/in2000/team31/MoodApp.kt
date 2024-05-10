package no.uio.ifi.in2000.team31

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import no.uio.ifi.in2000.team31.ui.home.HomeViewModel
import no.uio.ifi.in2000.team31.ui.navigation.AppNavigation
import no.uio.ifi.in2000.team31.ui.settings.SettingsViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MoodApp(homeViewModel: HomeViewModel, settingsViewModel: SettingsViewModel) {
    AppNavigation(homeViewModel = homeViewModel, settingsViewModel = settingsViewModel)
}