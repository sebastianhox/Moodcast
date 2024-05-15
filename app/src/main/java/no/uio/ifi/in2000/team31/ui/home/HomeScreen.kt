package no.uio.ifi.in2000.team31.ui.home

import android.os.Build
import android.util.Log
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.MoodApplication
import no.uio.ifi.in2000.team31.R
import no.uio.ifi.in2000.team31.cache.CachePolicy
import no.uio.ifi.in2000.team31.getWeatherStatus
import no.uio.ifi.in2000.team31.model.AlertIconModel
import no.uio.ifi.in2000.team31.model.WeatherIconMapper
import no.uio.ifi.in2000.team31.ui.navigation.AppRoutes
import no.uio.ifi.in2000.team31.ui.navigation.BottomNavigationBar
import no.uio.ifi.in2000.team31.ui.settings.celsiusToFahrenheit
import java.time.Clock
import java.time.LocalDate
import java.util.logging.Handler
import kotlin.math.roundToInt


// har ikke fått været (ikoner osv) til å gjenspeiles i faktisk værmelding - må fikses -å


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel) {

    val appContainer = (LocalContext.current.applicationContext as MoodApplication).appContainer
    val sharedViewModel = appContainer.sharedViewModel
    val settingsViewModel = appContainer.settingsViewModel

    val weatherData by homeViewModel.weatherDataUIState.collectAsState()
    val searchUiState by homeViewModel.searchUiState.collectAsState()
    val locationState by sharedViewModel.locationUIState.collectAsState()
    val isFahrenheit by settingsViewModel.isFahrenheit.collectAsState()
    val darkModeOn by settingsViewModel.isDarkTheme.collectAsState()
    val locationOn by settingsViewModel.locationOn.collectAsState()


    // Midlertidig? Setter weatherstatus i sharedviewmodel til symbolkdoen fra api-kallet
    val weatherStatusString = weatherData.weatherData?.instant?.get(0)?.symbolCode
    val weatherStatus = getWeatherStatus(weatherStatusString)
    sharedViewModel.setCurrentWeatherStatus(weatherStatus)

    val tempAndTimeList = weatherData.tempAndTimeData

    val scrollState = rememberScrollState()
    val snackbarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val backgroundColor =
        if (darkModeOn) Color.DarkGray.copy(alpha = 0.85f) else Color.LightGray.copy(alpha = 0.85f)
    var temperature = weatherData.weatherData?.instant?.get(0)?.airTemperature
    val humidity = weatherData.weatherData?.instant?.get(0)?.relativeHumidity
    var symbol = "°C"
    if (isFahrenheit && temperature != null) { // Funker ikke enda
        Log.d("temp", "Temp is $temperature before convertion")
        temperature = celsiusToFahrenheit(temperature.toInt())?.toDouble()
        Log.d("temp", "Temp is $temperature after convertion")
        symbol = "°F"
    }

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }


    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1000)
        Log.d("refreshstuff", "Getting location for ${locationState.lat} ${locationState.lon}")
        homeViewModel.fetchWeatherData(
            locationState.lat,
            locationState.lon,
            CachePolicy(CachePolicy.Type.REFRESH)
        )

        //homeViewModel.fetchWeatherData()
        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = ::refresh)


    // Tegner bakgrunnsbildet først
    Box (
        modifier = Modifier.pullRefresh(refreshState)
    ) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarState
                ) { snackbarData ->
                    CustomSnackBar(
                        snackbarData.visuals.message,
                        navController = navController
                    )
                }
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Tegner bakgrunnsbildet først

                if (darkModeOn) {
                    Image(
                        painter = painterResource(id = R.drawable.wallpaper_darkmode),
                        contentDescription = "Background Image",
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.FillBounds
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.wallpaper_lightmode),
                        contentDescription = "Background Image",
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.FillBounds
                    )
                }

                Column {

                    SearchBar(
                        query = searchUiState.currentQuery,
                        onQueryChange = homeViewModel::onPlaceNameSearch,
                        onSearch = {},
                        active = searchUiState.isSearching,
                        onActiveChange = { homeViewModel.onToogleSearch() },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        trailingIcon = {
                            if (searchUiState.isSearching) {
                                IconButton(onClick = {
                                    if (searchUiState.currentQuery.isBlank()) {
                                        homeViewModel.onToogleSearch()
                                    } else {
                                        homeViewModel.onPlaceNameSearch("")
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Filled.Close,
                                        contentDescription = "Close Icon",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search Icon",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    ) {

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth()
                                .clickable {
                                    if (locationOn) {
                                        homeViewModel.fetchWeatherData(
                                            locationState.lat,
                                            locationState.lon,
                                            CachePolicy(CachePolicy.Type.REFRESH)
                                        )
                                        homeViewModel.clearSelectedPlace()
                                        homeViewModel.onToogleSearch()
                                    } else {
                                        scope.launch {
                                            snackbarState.showSnackbar("Posisjonsbasert værvarsel er slått av.\nDu kan slå den på igjen på instillinger")
                                        }
                                    }
                                },
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    top = 10.dp,
                                    end = 8.dp,
                                    bottom = 4.dp
                                )
                            ) {

                                Icon(
                                    imageVector = Icons.Outlined.MyLocation,
                                    contentDescription = "my location",
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                                Text(
                                    text = "Min posisjon",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }


                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 8.dp)
                        ) {
                            items(searchUiState.places) { place ->
                                Column(
                                    Modifier
                                        .padding(top = 6.dp, bottom = 6.dp)
                                        .fillMaxSize()
                                        .clickable(
                                            onClick = {
                                                homeViewModel.onToogleSearch()
                                                homeViewModel.onPlaceSelected(place, CachePolicy(CachePolicy.Type.REFRESH))
                                            }
                                        ),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Card(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .fillMaxSize(),
                                        colors = CardDefaults.cardColors(Color.Transparent)
                                    ) {
                                        Text(
                                            text = "${place.placeName} ${place.adminName},  ${place.country}",
                                            modifier = Modifier.padding(
                                                start = 8.dp,
                                                top = 4.dp,
                                                end = 8.dp,
                                                bottom = 4.dp
                                            )
                                        )
                                    }

                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {

                        // Temperature right now, in celcius
                        Box(
                            modifier = Modifier
                                .width(360.dp)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.TopEnd
                                ) {

                                    Text(
                                        text = searchUiState.selectedPlace?.placeName
                                            ?: "Min posisjon",
                                        fontSize = 30.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.align(Alignment.Center),
                                        fontWeight = FontWeight.Bold
                                    )


                                    var dynamicTopPadding = 0
                                    var dynamicLPadding = 0
                                    var alertIconsCount = 0
                                    val alertIconsLimit = 3


                                    Box(
                                        modifier = Modifier
                                            .clickable {
                                                if (weatherData.alertIconData.isNotEmpty()) {
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
                                            }
                                    ) {
                                        weatherData.alertIconData.forEach { alertIconData ->
                                            if (alertIconsCount < alertIconsLimit) {
                                                Image(
                                                    painter = painterResource(id = AlertIconModel.eventIconMap[alertIconData.first + alertIconData.second]!!),
                                                    contentDescription = alertIconData.first + alertIconData.second,
                                                    modifier = Modifier
                                                        .align(Alignment.Center)
                                                        .padding(
                                                            top = dynamicTopPadding.dp,
                                                            end = dynamicLPadding.dp
                                                        )
                                                )
                                            }

                                            alertIconsCount += 1
                                            dynamicTopPadding += 7
                                            dynamicLPadding += 7
                                        }
                                    }
                                }
                                Image(
                                    painter = painterResource(
                                        id = WeatherIconMapper.symbolCodeMap[weatherData.weatherData?.instant?.first()?.symbolCode]
                                            ?: R.drawable.svg
                                    ),
                                    contentDescription = "weather icon",
                                    modifier = Modifier.size(80.dp),
                                    alignment = Alignment.Center
                                )
                                Box(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = temperature?.let { "${it.roundToInt()}" + symbol }
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
                                .align(Alignment.CenterHorizontally)
                                .padding(30.dp)
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(
                                    backgroundColor,
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
                                    text = "Neste 24 timer",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }

                        LazyRow(
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxHeight(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(tempAndTimeList) { hourlyData ->
                                val hour = hourlyData.first
                                var temp = hourlyData.second
                                val symbolCode = hourlyData.third

                                if (isFahrenheit) {
                                    temp = celsiusToFahrenheit(temp?.roundToInt())?.toDouble()
                                }
                                TimeAndTempCards(
                                    hour,
                                    temp,
                                    symbolCode
                                )
                            }
                        }
                    }
                    //Langtidsvarsel
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .shadow(50.dp)
                            .padding(horizontal = 30.dp)
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(
                                backgroundColor,
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
                                    Log.d("test", weatherData.longTermForecast.toString())
                                    weatherData.longTermForecast!!.entries.drop(1)
                                        .forEachIndexed { index, (day, forecastData) ->
                                            // Displays the forecast row
                                            Log.d("test", forecastData.first.toString())
                                            //day: String, symbolCode: String?, minTemp: Double, maxTemp: Double
                                            val symbolCode = forecastData.first
                                            var minTemp = forecastData.second
                                            var maxTemp = forecastData.third
                                            if (isFahrenheit) {
                                                minTemp = celsiusToFahrenheit(minTemp.roundToInt())?.toDouble()!!
                                                maxTemp = celsiusToFahrenheit(maxTemp.roundToInt())?.toDouble()!!
                                            }
                                            LongTermForecastRow(
                                                day,
                                                symbolCode,
                                                minTemp,
                                                maxTemp
                                            )

                                                // Adds a horizontal divider between rows
                                                if (index < weatherData.longTermForecast!!.size - 1) {
                                                    Spacer(modifier = Modifier.height(5.dp))
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(1.dp)
                                                            .background(
                                                                Color.White.copy(
                                                                    alpha = 0.5f
                                                                )
                                                            )

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
        PullRefreshIndicator(
            refreshing,
            refreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LongTermForecastRow(
    day: String,
    symbolCode: String?,
    minTemp: Double,
    maxTemp: Double,
) {
    val locale = java.util.Locale("no", "NO") // Norwegian locale
    val currentDate = LocalDate.now(Clock.systemDefaultZone())
    val localDate = LocalDate.parse(day)

    if (localDate >= currentDate) {
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
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(
                    id = WeatherIconMapper.symbolCodeMap[symbolCode] ?: R.drawable.svg
                ),
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .size(20.dp)
            )
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .width(90.dp),
                contentAlignment = Alignment.CenterEnd

            ) {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${minTemp.roundToInt()}°")
                        }
                        append("  |  ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${maxTemp.roundToInt()}°")

                        }
                    },
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimeAndTempCards(
    hour: String?,
    temperature: Double?,
    symbolCode: String?,
) {

    Spacer(modifier = Modifier.height(10.dp))


    Column(
        modifier = Modifier
            .padding(2.dp)
            .height(200.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Time and weather icon
        Box(
            modifier = Modifier
                .height(150.dp)
                .width(100.dp)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(25.dp))

                Text(
                    text = "$hour",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Weather icon
                Image(
                    painter = painterResource(id = WeatherIconMapper.symbolCodeMap[symbolCode]!!),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Temperature
                Text(
                    text = " ${temperature?.roundToInt()}°",
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,

                    )
            }
        }
    }
}

@Composable
fun CustomSnackBar(
    message: String,
    isRtl: Boolean = false,
    containerColor: Color = Color.White,
    contentColor: Color = Color.Black,
    navController: NavController,
) {
    Snackbar(
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
                Text(
                    modifier = Modifier.clickable {
                        navController.navigate(AppRoutes.SETTINGS) {
                            if (navController.currentDestination?.route != AppRoutes.SETTINGS) {
                                navController.navigate(AppRoutes.SETTINGS) {
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
                    text = "Til instillinger"
                )
            }

        }

    }
}

