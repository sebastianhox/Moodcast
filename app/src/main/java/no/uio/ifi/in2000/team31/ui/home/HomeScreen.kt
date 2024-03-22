package no.uio.ifi.in2000.team31.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun DisplayLocation(locationViewModel: HomeViewModel) {
    val context = LocalContext.current
    //val repository = LocationWeatherRepository()
    //val locationViewModel: LocationViewModel = viewModel()
    val weatherData = locationViewModel.weatherData.collectAsState().value
    val weatherAlert by locationViewModel.weatherAlertUIState.collectAsState()

    val temperature = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.airTemperature
    val humidity = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.relativeHumidity
    val rainAmount = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.precipitationAmount
    val windSpeed = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.windSpeed
    val windDirection = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.windFromDirection
    val airPressure = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.airPressureAtSeaLevel
    val cloudCover = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.cloudAreaFraction

    LaunchedEffect(key1 = true) {
        //if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        locationViewModel.startLocationUpdates()

        /*
        Her bør vi egentlig kalle startAlertUpdates() med lat og lon fra enheten, via locationViewModel
        Men bruker default koordinater som er lagt inn i funksjonen
        Som viser til et sted på vestlandet
        Som passer bedre pga ofte dårlig vær borti der
        */
        locationViewModel.startAlertUpdates()
        //}
    }

    val permissionGranted = locationViewModel.permissionGranted.collectAsState().value

    LaunchedEffect(key1 = permissionGranted) {
        if (permissionGranted/* && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED*/) {
            locationViewModel.checkPermissionsAndStartUpdates(context)
            //locationViewModel.startLocationUpdates()
        }
    }

    val locationState = locationViewModel.locationState.collectAsState()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (weatherData != null) {

            for (feature in weatherAlert.features) {
                Text(
                    text = "${feature.properties["instruction"]}\n",
                    //color = Color.Red
                )
            }
            Text(
                text = "Latitude: ${locationState.value.first}\nLongitude: ${locationState.value.second}",
                fontSize = 24.sp
            )
            Text(
                text = temperature?.let { "$it°C" } ?: "Temperature unavailable",
                textAlign = TextAlign.Center,
                fontSize = 36.sp
            )
            Text(
                text = humidity?.let { "$it%" } ?: "Humidity unavailable",
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            Text(
                text = rainAmount?.let { "$it" } ?: "No rain",
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
            Text(
                text = windSpeed?.let { "$it m/s" } ?: "Wind unavailable",
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        } else {
            Text("No weather data yet...")
        }
    }
}