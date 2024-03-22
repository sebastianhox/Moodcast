package no.uio.ifi.in2000.team31

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import no.uio.ifi.in2000.team31.ui.home.HomeScreen
import no.uio.ifi.in2000.team31.ui.home.HomeViewModel
import no.uio.ifi.in2000.team31.ui.theme.Team31Theme

class MainActivity : ComponentActivity() {
    val locationViewModel: LocationViewModel by viewModels()
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        locationViewModel.startLocationUpdates()
        locationViewModel.startAlertUpdates()


        if (granted) {
            Log.d("testing", "Permission granted")
        } else {
            Log.d("testing", "Permission denied")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()

        setContent {
            Team31Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //requestPermissions()
                    //DisplayLocation()
                    val navController = rememberNavController()
                    NavHost(navController, "DisplayLocation") {
                        composable("DisplayLocation") { DisplayLocation(locationViewModel) }
                        composable("HomeScreen") { HomeScreen() }
                    }
                }
                //requestPermissions()
            }
        }
    }

    private fun requestPermissions() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

        )
    }
}

@Composable
fun DisplayLocation(locationViewModel: LocationViewModel) {
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
            /*
            Text(
                "Temperature: ${weatherData.properties?.timeseries?.get(0)?.data?.instant?.details?.airTemperature}",
                fontSize = 48.sp
            )*/

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





/*
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var homeViewModel: HomeViewModel

    // TEST
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallBack: LocationCallback
    private var currentLocation: Location? = null
    // TEST

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Log.d("testing", "ACCESS_FINE_LOCATION granted")
                fetchLocationAndUpdateWeather()
            }
            else -> Log.d("testing", "ACCESS_FINE_LOCATION denied")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("testing", "onCreate()")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        setContent {
            Team31Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val startDestination: String = "HomeScreen"
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "HomeScreen") {
                        composable("HomeScreen") { runHomeScreen() }
                        composable("LongTermForecast") { TODO() }
                    }


                }
            }
        }
    }



    private fun fetchLocationAndUpdateWeather() {
        Log.d("testing", "Inne i fetchLocationAndUpdateWeather")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d("testing", "Inne i if'en")
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                Log.d("testing", "location er: $location")
                location?.let {
                    Log.d("testing", "Inne i let'en")
                    homeViewModel.updateWeatherData(it.latitude, it.longitude)
                }
            }
        } else {
            Log.d("testing", "Inne i else'en")
        }
    }

    @Composable
    fun runHomeScreen() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("testing", "Har tilgang")
            fetchLocationAndUpdateWeather()
            Log.d("testing", "Før HomeScreen()")
            HomeScreen(homeViewModel)
            Log.d("testing", "Etter HomeScreen()")
        } else {
            Log.d("testing", "Har ikke tilgang")
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            HomeScreen(homeViewModel)
        }
    }
}

@Composable
fun AppNavHostTest() {
    val navController = rememberNavController()
    //NavHost(navController, startDestination = "homeScreen")


}
*/