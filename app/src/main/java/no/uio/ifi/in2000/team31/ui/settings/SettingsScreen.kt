package no.uio.ifi.in2000.team31.ui.settings

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.first
import no.uio.ifi.in2000.team31.ui.activity.MoodCastTopBar
import no.uio.ifi.in2000.team31.ui.navigation.BottomNavigationBar

fun celsiusToFahrenheit(degreeInCelsius: Int): Int {
    return ((9.0 / 5) * degreeInCelsius + 32).toInt()
}

@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    val isFahrenheit by settingsViewModel.isFahrenheit.collectAsState()
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
            SettingItem(title = "Dark theme", description = "Bruk dark theme", isDarkTheme) {it ->
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp)
        ,
        horizontalArrangement = Arrangement.End
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title)
            Text(description)
        }
        Switch(checked = isCheckedVal, onCheckedChange = {
            isCheckedVal = it
            onCheckedChange(it)
            Log.d("settings", "Switch: setting value to $it")
        })
    }
}