package no.uio.ifi.in2000.team31.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    Log.d("testing", "HomeScreen")
    val weatherUIState by homeViewModel.weatherUIState.collectAsState()
    val temperature = weatherUIState.weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.airTemperature
    val latLonText = if (weatherUIState.weatherData != null) {
        "Lat: ${weatherUIState.lat}, Lon: ${weatherUIState.lon}"
    } else "Location not available"

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = latLonText,
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
        Text(
            //text = "${weatherData.weatherData?.instant?.last()?.airTemperature}",
            text = temperature?.let { "$it°C" } ?: "Ingen værdata tilgjengelig",
            textAlign = TextAlign.Center,
            fontSize = 50.sp
        )
    }
}