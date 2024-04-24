package no.uio.ifi.in2000.team31.ui.mood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.team31.ui.navigation.AppRoutes
import no.uio.ifi.in2000.team31.ui.navigation.BottomNavigationBar
import kotlin.math.roundToInt

@Composable
fun MoodScreen(navController: NavController, moodViewModel: MoodViewModel = viewModel()) {

    val context = LocalContext.current
    val weatherData by moodViewModel.weatherDataUIState.collectAsState()
    Scaffold(
        bottomBar = {
            if (navController.currentDestination?.route != AppRoutes.ALERT) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(top = 32.dp, start = 18.dp, end = 18.dp, bottom = 18.dp)
        ) {
            //Text over the gray box
            Text(
                text = "Været nå",
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )

            //Gray Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray, shape = RoundedCornerShape(15.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Skyete",
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                Text(
                    text = "${weatherData.weatherData?.instant?.get(0)?.airTemperature?.roundToInt()}",
                    fontSize = 16.sp
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(26.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Title for the mood selection
                Text(
                    text = "Hvordan føler du deg?",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
                //First Row with two buttons
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // TODO Look at snackbar instead of toast
                    Button(onClick = {
                        Toast.makeText(context, "Valgt glad humør", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Glad")
                    }

                    Button(onClick = {
                        Toast.makeText(context, "Valgt trist humør", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Trist")
                    }
                }

                //Second Row with two buttons
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Button(onClick = {
                        Toast.makeText(context, "Valgt energisk humør", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Energisk")
                    }

                    Button(onClick = {
                        Toast.makeText(context, "Valgt rolig humør", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Rolig")
                    }
                }
            }
        }
    }
}