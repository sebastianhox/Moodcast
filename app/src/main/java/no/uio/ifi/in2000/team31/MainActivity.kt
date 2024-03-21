package no.uio.ifi.in2000.team31

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
