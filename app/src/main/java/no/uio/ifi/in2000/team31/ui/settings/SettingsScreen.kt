package no.uio.ifi.in2000.team31.ui.settings

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Room
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = settingsViewModel) {
        val darkThemeOn = settingsViewModel.isDarkTheme.first()
        settingsViewModel.onDarkThemeSwitchChange(darkThemeOn)

        val fahrenheitOn = settingsViewModel.isFahrenheit.first()
        settingsViewModel.onFahrenheitSwitchChange(fahrenheitOn)
    }
    Scaffold(
        topBar = { MoodCastTopBar() },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarState
            ) { snackbarData ->
                if (isDarkTheme) {
                    CustomSnackBar(
                        snackbarData.visuals.message,
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                } else {
                    CustomSnackBar(
                        snackbarData.visuals.message,
                    )
                }
            }
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) {innerPadding ->
        val posWindowColorPrim = if (isDarkTheme) Color(0xFF002571) else Color(0xFFAAD3FF)
        val posWindowColorSec = if (isDarkTheme) Color(0xFFAAD3FF) else Color(0xFF002571)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(posWindowColorPrim)
                    .height(140.dp)


            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {

                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        if (isDarkTheme) {
                            Image(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = "Location icon",
                                modifier = Modifier
                                    .size(36.dp),
                                colorFilter = ColorFilter.tint(Color.LightGray)
                            )
                        } else {
                            Image(
                                imageVector = Icons.Outlined.Room,
                                contentDescription = "Location icon",
                                modifier = Modifier
                                    .size(36.dp)
                            )
                        }

                        if (locationOn) {
                            Text(
                                text = "Posisjonsbasert værvarsel er på",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        } else {
                            Text(
                                text = "Skru på posisjonsbasert værvarsel",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                    if (locationOn) {
                        Text(
                            text = "Tilgang til å bruke lokasjonstjenester styres av instillinger i Android. Åpne systeminstillingene for å endre dette",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    } else {
                        Button(
                            onClick = { settingsViewModel.onLocationSwitchChange(true)},
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            colors = ButtonDefaults.buttonColors(posWindowColorSec)){
                            Text(text = "Trykk her")
                        }
                    }
                }
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
            Text(
                text = "Enhet",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal =  32.dp, vertical = 8.dp))
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
            Switch(
                checked = isCheckedVal,
                onCheckedChange = {
                isCheckedVal = it
                onCheckedChange(it)
                Log.d("settings", "Switch: setting value to $it")
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF002571),
                    checkedTrackColor = Color(0xFFAAD3FF)
                )
            )
        }
    }
}

@Composable
fun CustomSnackBar(
    message: String,
    isRtl: Boolean = false,
    containerColor: Color = Color.Gray,
    contentColor: Color = Color.White,
) {
    Snackbar(
        modifier = Modifier.padding(8.dp),
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        CompositionLocalProvider(
            LocalLayoutDirection provides
                    if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
        ) {
            Row {
                Text(message)
                Spacer(modifier = Modifier.weight(1f))
            }

        }

    }
}