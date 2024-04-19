package no.uio.ifi.in2000.team31.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import no.uio.ifi.in2000.team31.R
import java.time.LocalDate
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel, navController: NavController) {
    val context = LocalContext.current
    val weatherData by homeViewModel.weatherDataUIState.collectAsState()
    val weatherAlert by homeViewModel.weatherAlertUIState.collectAsState()
    val symbolData by homeViewModel.symbolUIState.collectAsState()

    val symbolList = symbolData.symbolData

    Log.d("symbols:", "$symbolList")

    val tempAndTimeList = weatherData.tempAndTimeData
    val scrollState = rememberScrollState()
/*
    val temperature = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.airTemperature
    val humidity = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.relativeHumidity
    val rainAmount = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.precipitationAmount
    val windSpeed = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.windSpeed
    val windDirection = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.windFromDirection
    val airPressure = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.airPressureAtSeaLevel
    val cloudCover = weatherData?.properties?.timeseries?.get(0)?.data?.instant?.details?.cloudAreaFraction*/
    val temperature = weatherData.weatherData?.instant?.get(0)?.airTemperature


    LaunchedEffect(key1 = true) {
        homeViewModel.startLocationUpdates()
        homeViewModel.startAlertUpdates()
    }

    val permissionGranted by homeViewModel.permissionGranted.collectAsState()

    LaunchedEffect(key1 = permissionGranted) {
        if (permissionGranted) {
            homeViewModel.checkPermissionsAndStartUpdates(context)
        }
    }

    //val locationState = homeViewModel.locationState.collectAsState()
    Column(
        modifier = Modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        //Været akkurat nå i celsius
        Box(modifier = Modifier
            .padding(18.dp)
            .width(360.dp)
            .height(149.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(size = 15.dp))) {

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(modifier = Modifier.fillMaxWidth()) {

                    Text(
                        text = "Min posisjon",
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        //text = "${weatherData?.weatherData?.instant?.last()?.airTemperature}" + "\u00B0",
                        text = temperature?.let { "${it.roundToInt()}°" } ?: "Henter data...",
                        fontSize = 50.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        //Vær i dag - time for time
        Box(modifier = Modifier
            .padding(18.dp)
            .width(360.dp)
            .height(149.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(size = 15.dp))) {


            LazyRow(
                modifier = Modifier.fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tempAndTimeList?.size?.let {
                    items(tempAndTimeList) { map ->
                        TimeAndTempCards(map, homeViewModel)
                    }
                }
            }
        }
        //Langtidsvarsel
        Box(
            modifier = Modifier
                .padding(18.dp)
                .width(360.dp)
                .height(230.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(size = 15.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Langtidsvarsel:",
                    fontSize = 20.sp
                )

                // Check if long-term forecast data is available
                if (weatherData.longTermForecast != null) {
                    // Create a vertical scrollable column for forecast rows
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        // Loop over the long-term forecast data to display each row
                        weatherData.longTermForecast!!.entries.drop(1).forEach { (day, temps) ->
                            LongTermForecastRow(day, temps.first, temps.second)
                        }
                    }
                } else {
                    // Display a placeholder if forecast data is not available
                    Text(
                        text = "No forecast data available",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LongTermForecastRow(day: String, minTemp: Double, maxTemp: Double) {
    val locale = java.util.Locale("no", "NO") // Norwegian locale
    val currentDate = LocalDate.now()
    val localDate = LocalDate.parse(day)

    val dayOfWeek =
        if (localDate == currentDate) {
            "I dag"
    } else {
        localDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, locale).capitalize() // ?_?
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = dayOfWeek)
        Text(text = "${minTemp.roundToInt()}° | ${maxTemp.roundToInt()}°")
    }
}

@Composable
fun TimeAndTempCards(map: Map<String, Double>, homeViewModel: HomeViewModel) {
    Card(
        modifier = Modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        //colors = CardDefaults.cardColors(Color.Green)

    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //Tid
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(
                        text = map.keys.first()
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "${map.values.first().roundToInt()}" + "\u00B0"
                    )

                }
            }
        }
    }
}
