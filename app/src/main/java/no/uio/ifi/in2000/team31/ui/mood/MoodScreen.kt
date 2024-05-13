package no.uio.ifi.in2000.team31.ui.mood

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.MoodApplication
import no.uio.ifi.in2000.team31.R
import no.uio.ifi.in2000.team31.SharedViewModel
import no.uio.ifi.in2000.team31.model.WeatherIconMapper
import no.uio.ifi.in2000.team31.ui.activity.MoodCastTopBar
import no.uio.ifi.in2000.team31.ui.navigation.AppRoutes
import no.uio.ifi.in2000.team31.ui.navigation.BottomNavigationBar
import kotlin.math.roundToInt

enum class Mood {
    HAPPY,
    SAD,
    ENERGETIC,
    CALM
}

@Composable
fun MoodScreen(navController: NavController, moodViewModel: MoodViewModel = viewModel()) {
    val weatherData by moodViewModel.weatherDataUIState.collectAsState()
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val appContainer = (LocalContext.current.applicationContext as MoodApplication).appContainer
    val sharedViewModel = appContainer.sharedViewModel

    moodViewModel.manuallyUpdate()



    Scaffold(
        topBar = {
            MoodCastTopBar()
        },
        bottomBar = {
            if (navController.currentDestination?.route != AppRoutes.ALERT) {
                BottomNavigationBar(navController)
            }
        },
        // adds snackbarhost (toast outlawed)
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarState
                ) {snackbarData ->
                    CustomSnackBar(
                        snackbarData.visuals.message,
                        navController = navController
                    )

                }
            }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(top = 42.dp, start = 10.dp, end = 10.dp, bottom = 0.dp)
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
                    .padding(5.dp)

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = WeatherIconMapper.symbolCodeMap[weatherData.symbolCodeNow] ?: R.drawable.svg), // placeholder icon, hard coded
                        contentDescription = "Værikon",
                        modifier = Modifier.size(44.dp),

                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${weatherData.temperature?.roundToInt()}°C ",
                        fontSize = 25.sp,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(26.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "Hvordan føler du deg i dag?",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .padding(bottom = 5.dp)
                        .align(Alignment.CenterHorizontally)

                )

                // Humørknapper
                MoodButton("😊 Glad", Color(0xFFFFCC00), Color.White, scope, snackbarState, sharedViewModel, Mood.HAPPY)
                MoodButton("😢 Trist", Color(	0xFF3d9BFF), Color.White, scope, snackbarState, sharedViewModel, Mood.SAD)
                MoodButton("⚡ Energisk", Color(0xFFFF9500), Color.White, scope, snackbarState, sharedViewModel, Mood.ENERGETIC)
                MoodButton("🍃 Rolig", Color(0xFF8282DA), Color.White, scope, snackbarState, sharedViewModel, Mood.CALM)
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
    mood: Mood
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 62.dp, vertical = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .shadow(5.dp, RoundedCornerShape(55.dp)),
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
    isRtl: Boolean = false,
    containerColor: Color = Color.White,
    contentColor: Color = Color.Black,
    navController: NavController
) {
    Snackbar(
        containerColor = containerColor,
        contentColor = contentColor) {
        CompositionLocalProvider(
            LocalLayoutDirection provides
                    if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
        ) {
            Row {
                Text(message)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.clickable {
                        navController.navigate(AppRoutes.ACTIVITY) {
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
                        },
                    text = "Til aktiviteter"
                )
            }

        }
            
    }
}




