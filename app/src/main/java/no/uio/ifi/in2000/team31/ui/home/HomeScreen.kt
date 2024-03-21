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
import io.github.dellisd.spatialk.geojson.dsl.point
import kotlinx.coroutines.coroutineScope
import no.uio.ifi.in2000.team31.data.WeatherAlertRepository

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) {
    Log.d("testing", "HomeScreen")
    val weatherUIState by homeViewModel.weatherUIState.collectAsState()
    val temperature = weatherUIState.weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.airTemperature
    val humidity = weatherUIState.weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.relativeHumidity
    val rainAmount = weatherUIState.weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.precipitationAmount
    val windSpeed = weatherUIState.weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.windSpeed
    val windDirection = weatherUIState.weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.windFromDirection
    val airPressure = weatherUIState.weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.airPressureAtSeaLevel
    val cloudCover = weatherUIState.weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.cloudAreaFraction






    val latLonText = if (weatherUIState.weatherData != null) {
        "Latitude: ${weatherUIState.lat}\nLongitude: ${weatherUIState.lon}"
    } else "Not found"

    val alertRepository: WeatherAlertRepository = WeatherAlertRepository()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = latLonText,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
        Text(
            //text = "${weatherData.weatherData?.instant?.last()?.airTemperature}",
            text = temperature?.let { "$it°C" } ?: "Temperature unavailable",
            textAlign = TextAlign.Center,
            fontSize = 36.sp
        )
        Text(
            //text = "${weatherData.weatherData?.instant?.last()?.airTemperature}",
            text = humidity?.let { "$it%" } ?: "Humidity unavailable",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
        Text(
            //text = "${weatherData.weatherData?.instant?.last()?.airTemperature}",
            text = rainAmount?.let { "$it" } ?: "No rain",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
        Text(
            //text = "${weatherData.weatherData?.instant?.last()?.airTemperature}",
            text = windSpeed?.let { "$it m/s" } ?: "Wind unavailable",
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
    }
}

// Dette har ikke blitt testet og kan inneholde mye dritt
suspend fun getAlertStuff(repository: WeatherAlertRepository) {
    val zones = repository.getDangerZonesOf(point(6.352810,59.650822))
    for (zone in zones) {
        Log.d("alertTest", zone.properties.toString())
    }
}
