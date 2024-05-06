package no.uio.ifi.in2000.team31.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import no.uio.ifi.in2000.team31.R
import no.uio.ifi.in2000.team31.model.AlertIconModel
import no.uio.ifi.in2000.team31.ui.navigation.AppRoutes
import no.uio.ifi.in2000.team31.ui.navigation.BottomNavigationBar
import java.time.LocalDate
import kotlin.math.roundToInt


// har ikke fått været (ikoner osv) til å gjenspeiles i faktisk værmelding - må fikses -å


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel) {
    val weatherData by homeViewModel.weatherDataUIState.collectAsState()
    val searchUiState by homeViewModel.searchUiState.collectAsState()

    val tempAndTimeList = weatherData.tempAndTimeData
    val scrollState = rememberScrollState()
    val temperature = weatherData.weatherData?.instant?.get(0)?.airTemperature

    // Background image (PLACEHOLDER ENN SÅ LENGE]
    val backgroundImageUrl ="https://img.freepik.com/free-vector/gradient-mountain-landscape_23-2149162009.jpg?size=626&ext=jpg&ga=GA1.1.553209589.1714608000&semt=sph"

    // Weather icon (sun for now)
    val sunnyWeather = painterResource(R.drawable.sun_icon)
    val rainyWeather = painterResource(R.drawable.rain_icon)



    Scaffold(
        topBar = {
                 SearchBar(
                     query = searchUiState.currentQuery,
                     onQueryChange = homeViewModel::onPlaceNameSearch,
                     onSearch = homeViewModel::onPlaceNameSearch,
                     active = searchUiState.isSearching,
                     onActiveChange = { homeViewModel.onToogleSearch() }
                 ) {
                     LazyColumn {
                         items(searchUiState.places) { place ->
                             Text(
                                 text = "${place.placeName} ${place.adminName},  ${place.country}",
                                 modifier = Modifier.padding(
                                     start = 8.dp,
                                     top = 4.dp,
                                     end = 8.dp,
                                     bottom = 4.dp)
                             )
                         }
                     }
                     
                 }
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tegner bakgrunnsbildet først
            AsyncImage(
                model = backgroundImageUrl,
                contentDescription = "Background Image",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Temperature right now, in celcius
                Box(
                    modifier = Modifier
                        .padding(18.dp)
                        .width(360.dp)
                        .height(149.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.TopEnd
                        ) {

                            Text(
                                text = "Min posisjon",
                                fontSize = 25.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )


                            var dynamicBotPadding = 0
                            var dynamicLPadding = 0

                            Box(
                                modifier = Modifier
                                    .clickable {
                                        navController.navigate(AppRoutes.ALERT) {
                                            // fikser backstack
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }

                                            //unngår flere instanser
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                            ) {
                                weatherData.features?.forEach { feature ->
                                    val event = feature.getStringProperty("event")
                                    val color = feature.getStringProperty("riskMatrixColor")

                                    Image(
                                        painter = painterResource(id = AlertIconModel.eventIconMap[event + color]!!),
                                        contentDescription = event + color,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(
                                                top = dynamicBotPadding.dp,
                                                end = dynamicLPadding.dp
                                            )
                                    )
                                    dynamicBotPadding += 3
                                    dynamicLPadding += 3
                                }
                            }


                        }
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                //text = "${weatherData?.weatherData?.instant?.last()?.airTemperature}" + "\u00B0",
                                text = temperature?.let { "${it.roundToInt()}°" }
                                    ?: "Henter data...",
                                fontSize = 50.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }

                //Vær i dag - time for time
                Box(
                    modifier = Modifier
                        .padding(18.dp)
                        .width(400.dp)
                        .height(200.dp)
                        .background(
                            Color.LightGray.copy(alpha = 0.95f),
                            shape = RoundedCornerShape(size = 15.dp)
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            )
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "I dag",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxHeight(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        tempAndTimeList?.size?.let {
                            items(tempAndTimeList) { map ->
                                TimeAndTempCards(map, temperature, sunnyWeather, rainyWeather)
                            }
                        }
                    }
                }
                //Langtidsvarsel
                Box(
                    modifier = Modifier
                        .padding(48.dp)
                        .width(400.dp)
                        .shadow(50.dp)
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(
                            Color.LightGray.copy(alpha = 0.95f),
                            shape = RoundedCornerShape(size = 15.dp)
                        )

                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                                text = "Langtidsvarsel",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()

                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Check if long-term forecast data is available
                        if (weatherData.longTermForecast != null) {
                            // Create a vertical scrollable column for forecast rows
                            Column(
                                modifier = Modifier.verticalScroll(rememberScrollState())
                            ) {
                                // Loop over the long-term forecast data to display each row
                                weatherData.longTermForecast!!.entries.drop(1)
                                    .forEachIndexed { index, (day, temps) ->
                                        // Displays the forecast row
                                        LongTermForecastRow(day, temps.first, temps.second)

                                        // Adds a horizontal divider between rows
                                        if (index < weatherData.longTermForecast!!.size - 1) {
                                            Spacer(modifier = Modifier.height(5.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(1.dp)
                                                    .background(Color.White.copy(alpha = 0.5f))

                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
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
                localDate.dayOfWeek.getDisplayName(java.time.format.TextStyle.FULL, locale)
                    .replaceFirstChar(Char::titlecase)
            }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dayOfWeek,
                fontWeight = FontWeight.Bold
            )


            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${minTemp.roundToInt()}°")
                    }
                    append(" | ")
                    withStyle(style = SpanStyle(color = Color.Red, fontWeight = FontWeight.Bold)) {
                        append("${maxTemp.roundToInt()}°")
                    }
                }
            )
        }
    }



@Composable
fun TimeAndTempCards(
    map: Map<String, Double>,
    temperature: Double?,
    sunnyWeatherPainter: Painter,
    rainyWeatherPainter: Painter
) {


    // OBS OBS DENNE LOGIKKEN ER FUCKED OG FUNKER IKKE -Å
    val weatherIcon = when {
        temperature != null -> {
            when {
                temperature >= 20 -> sunnyWeatherPainter
                temperature <= 10 -> rainyWeatherPainter
                else -> sunnyWeatherPainter
            }
        }
        else -> sunnyWeatherPainter // Bruk solikon som standardikon hvis temperaturen er ukjent
    }

    Spacer(modifier = Modifier.height(10.dp))


    Column(
        modifier = Modifier
            .padding(4.dp)
            .height(120.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tid og værikon
        Box(
            modifier = Modifier
                .size(100.dp)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    text = map.keys.first()
                )

                Spacer(modifier = Modifier.height(10.dp))


                // Weathericon
                Image(
                    painter = weatherIcon,
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(30.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Temperature
                Text(
                    text = "${map.values.first().roundToInt()}°",
                    fontSize = 22.sp
                )
            }
        }
    }
}


