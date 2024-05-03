package no.uio.ifi.in2000.team31.ui.mood

import android.content.Context
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
            Text(
                text = "Været nå",
                fontSize = 24.sp,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(12.dp)

            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.WbSunny, // placeholder ikon, hardkodet
                        contentDescription = "Værikon",
                        modifier = Modifier.size(34.dp),
                        tint = Color.Yellow
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${weatherData.weatherData?.instant?.get(0)?.airTemperature?.roundToInt()}°C Solrikt",
                        fontSize = 25.sp
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
                    text = "Hvordan føler du deg?",
                    fontSize = 30.sp,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )

                // Humørknapper
                MoodButton("😊 Glad", Color(0xFFFFCC00), Color.White, context)
                MoodButton("😢 Trist", Color(0xFF007AFF), Color.White, context)
                MoodButton("⚡ Energisk", Color(0xFFFF9500), Color.White, context)
                MoodButton("🍃 Rolig", Color(0xFF8282DA), Color.White, context)
            }
        }
    }
}

@Composable
fun MoodButton(text: String, backgroundColor: Color, textColor: Color, context: Context) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .shadow(5.dp, RoundedCornerShape(25.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        onClick = {
            Toast.makeText(context, "Valgt $text humør", Toast.LENGTH_SHORT).show()
        }
    ) {
        Text(text, fontSize = 35.sp, color = textColor)
    }
}




