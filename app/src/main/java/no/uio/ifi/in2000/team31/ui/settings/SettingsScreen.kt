package no.uio.ifi.in2000.team31.ui.settings

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.first
import no.uio.ifi.in2000.team31.MoodApplication
import no.uio.ifi.in2000.team31.ui.activity.MoodCastTopBar
import no.uio.ifi.in2000.team31.ui.navigation.BottomNavigationBar

fun celsiusToFahrenheit(degreeInCelsius: Int?): Int? {
    if (degreeInCelsius == null) {
        return null
    }
    return ((9.0 / 5) * degreeInCelsius + 32).toInt()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(navController: NavController) {

    val appContainer = (LocalContext.current.applicationContext as MoodApplication).appContainer
    val settingsViewModel = appContainer.settingsViewModel
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    val isFahrenheit by settingsViewModel.isFahrenheit.collectAsState()
    val locationOn by settingsViewModel.locationOn.collectAsState()
    LaunchedEffect(key1 = settingsViewModel) {
        val darkThemeOn = settingsViewModel.isDarkTheme.first()
        settingsViewModel.onDarkThemeSwitchChange(darkThemeOn)

        val fahrenheitOn = settingsViewModel.isFahrenheit.first()
        settingsViewModel.onFahrenheitSwitchChange(fahrenheitOn)
    }
    Scaffold(
        topBar = { MoodCastTopBar() },
        bottomBar = { BottomNavigationBar(navController) }
    ) {padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Text(
                text = "Posisjon",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal =  32.dp, vertical = 8.dp))

            SettingItem(
                title = "Posisjonsbasert værvarsel",
                description = "Gi tilgang til enhetens posisjon",
                locationOn
            ) {
                settingsViewModel.onLocationSwitchChange(it)
                Log.d("settings", "Passing $locationOn to switch")
            }

            Text(
                text = "Tema",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal =  32.dp, vertical = 8.dp))

            SettingItem(title = "Dark theme", description = "Bruk dark theme", isDarkTheme) {
                //settingsViewModel.toggleDarkTheme()
                settingsViewModel.onDarkThemeSwitchChange(it)
                Log.d("settings", "Passing $isDarkTheme to switch")
            }
            SettingItem(title = "Fahrenheit", description = "Bruk fahrenheit", isFahrenheit) {
                settingsViewModel.onFahrenheitSwitchChange(it)
                Log.d("settings", "Passing $isFahrenheit to switch")
            }

        }
    }
}

@Composable
fun SettingItem(title: String, description: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    var isCheckedVal by remember { mutableStateOf(isChecked) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(100.dp)
            .shadow(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Column() {
                Text(title)
                Text(description)
            }
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = isCheckedVal, onCheckedChange = {
                isCheckedVal = it
                onCheckedChange(it)
                Log.d("settings", "Switch: setting value to $it")
            })
        }
    }
}