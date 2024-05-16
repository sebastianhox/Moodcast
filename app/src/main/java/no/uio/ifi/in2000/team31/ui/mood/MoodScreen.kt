package no.uio.ifi.in2000.team31.ui.mood

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.container.MoodApplication
import no.uio.ifi.in2000.team31.R
import no.uio.ifi.in2000.team31.ui.shared.SharedViewModel
import no.uio.ifi.in2000.team31.data.network.Status
import no.uio.ifi.in2000.team31.model.WeatherIconMapper
import no.uio.ifi.in2000.team31.ui.activity.MoodCastTopBar
import no.uio.ifi.in2000.team31.ui.navigation.AppRoutes
import no.uio.ifi.in2000.team31.ui.navigation.BottomNavigationBar
import no.uio.ifi.in2000.team31.ui.settings.celsiusToFahrenheit
import kotlin.math.roundToInt

enum class Mood {
    GLAD,
    TRIST,
    ENERGISK,
    ROLIG,
    STRESSET,
    SINT
}

@Composable
fun MoodScreen(navController: NavController, moodViewModel: MoodViewModel = viewModel()) {
    val weatherData by moodViewModel.weatherDataUIState.collectAsState()
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val appContainer = (LocalContext.current.applicationContext as MoodApplication).appContainer
    val sharedViewModel = appContainer.sharedViewModel
    val settingsViewModel = appContainer.settingsViewModel

    moodViewModel.manuallyUpdate()

    var temperature = weatherData.temperature
    var symbol = "°C"

    val connectionState by sharedViewModel.connectionStatus.collectAsState(
        initial = Status.Unavailable
    )
    val isDarkMode by settingsViewModel.isDarkTheme.collectAsState()
    val isFahrenheit by settingsViewModel.isFahrenheit.collectAsState()
    if (isFahrenheit) {
        temperature = celsiusToFahrenheit(temperature?.roundToInt())?.toDouble()
        symbol = "°F"
    }


    Scaffold(
        topBar = {
            MoodCastTopBar()
        },
        bottomBar = { BottomNavigationBar(navController) },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarState,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) { snackbarData ->
                if (isDarkMode) {
                    CustomSnackBar(
                        snackbarData.visuals.message,
                        navController = navController,
                        contentColor = Color(0xFFAAD3FF),
                        containerColor = Color(0xFF002571)
                    )
                } else {
                    CustomSnackBar(
                        snackbarData.visuals.message,
                        navController = navController,
                        contentColor = Color(0xFF002571),
                        containerColor = Color(0xFFAAD3FF)
                    )
                }
            }
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(top = 16.dp, start = 10.dp, end = 10.dp, bottom = 0.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Været nå",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
                    .padding(top = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .padding(4.dp)

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (connectionState == Status.Available) {
                        Image(
                            painter = painterResource(
                                id = WeatherIconMapper.symbolCodeMap[weatherData.symbolCodeNow]
                                    ?: R.drawable.svg
                            ),
                            contentDescription = "Værikon",
                            modifier = Modifier.size(42.dp),

                            )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "${temperature?.roundToInt()}" + symbol,
                            fontSize = 24.sp,
                        )
                    } else {
                        val colorPrim = if (isDarkMode) Color(0xFF002571) else Color(0xFFAAD3FF)
                        Text(
                            text = "Værdata ikke tilgjengelig\nMangler internett tilgang",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(colorPrim)
                                .padding(20.dp)

                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "Hvordan føler du deg?",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(bottom = 5.dp)
                        .align(Alignment.CenterHorizontally)

                )

                MoodButton(
                    "😊 Glad",
                    Color(0xFFFFCC00),
                    Color.White,
                    scope,
                    snackbarState,
                    sharedViewModel,
                    Mood.HAPPY
                )
                MoodButton(
                    "⚡ Energisk",
                    Color(0xFFFF9500),
                    Color.White,
                    scope,
                    snackbarState,
                    sharedViewModel,
                    Mood.ENERGETIC
                )
                MoodButton(
                    "🍃 Rolig",
                    Color(0xFF8282DA),
                    Color.White,
                    scope,
                    snackbarState,
                    sharedViewModel,
                    Mood.CALM
                )
                MoodButton(
                    "😢 Trist",
                    Color(0xFF3d9BFF),
                    Color.White,
                    scope,
                    snackbarState,
                    sharedViewModel,
                    Mood.SAD
                )
                MoodButton(
                    "😰 Stresset",
                    Color(0xFF4CAF50),
                    Color.White,
                    scope,
                    snackbarState,
                    sharedViewModel,
                    Mood.STRESSED
                )
                MoodButton(
                    "😠 Sint",
                    Color(0xFFAD0909),
                    Color.White,
                    scope,
                    snackbarState,
                    sharedViewModel,
                    Mood.ANGRY
                )

            }
        }
    }
}

@Composable
fun MoodButton(
    text: String,
    backgroundColor: Color,
    textColor: Color,
    scope: CoroutineScope,
    snackbarState: SnackbarHostState,
    sharedViewModel: SharedViewModel,
    mood: Mood,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp)
            .padding(horizontal = 32.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .shadow(5.dp, RoundedCornerShape(56.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        onClick = {
            sharedViewModel.setSelectedMood(mood)
            Log.d("moodfix", "Setting the mood to $mood")
            scope.launch {
                snackbarState.showSnackbar("Valgt humør: $text ")
            }
        }
    ) {
        Text(text, fontSize = 25.sp, color = textColor)
    }
}

@Composable
fun CustomSnackBar(
    message: String,
    containerColor: Color = Color.White,
    contentColor: Color = Color.Black,
    navController: NavController,
) {
    Snackbar(
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    message,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 10.dp),
                    onClick = {
                        navController.navigate(AppRoutes.ACTIVITY) {
                            if (navController.currentDestination?.route != AppRoutes.ACTIVITY) {
                                navController.navigate(AppRoutes.ACTIVITY) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }

                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = contentColor
                    )
                ) {
                    Text(
                        text = "Til aktiviteter",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.End
                    )
                }
            }
        }

    }
}




