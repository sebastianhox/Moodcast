package no.uio.ifi.in2000.team31

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.team31.ui.home.HomeViewModel
import no.uio.ifi.in2000.team31.ui.navigation.AppNavigation
import no.uio.ifi.in2000.team31.ui.settings.SettingsViewModel
import no.uio.ifi.in2000.team31.ui.theme.Team31Theme

class MainActivity : ComponentActivity() {
    val homeViewModel: HomeViewModel by viewModels()
    val settingsViewModel: SettingsViewModel = SettingsViewModel()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Team31Theme(settingsViewModel.isDarkTheme.collectAsState().value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SetupNavGraph(navController, homeViewModel, settingsViewModel)
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.locations.firstOrNull()
                Log.d("location", "Fetched location: ${location?.latitude}, ${location?.longitude}")
                if (location != null) {
                    homeViewModel.fetchWeatherData(location.latitude, location.longitude)
                } else {
                    Log.w("location", "Could not access the user's location")
                }
            }
        }

        requestLocationAndStartUpdates()
    }

    private fun requestLocationAndStartUpdates() {
        Log.d("location", "Start location permission request / updates")

        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val locationRequest =
            LocationRequest.Builder(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                3600000
            ).build()

        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("location", "Requesting permissions")
            requestPermissionLauncher.launch(
                permission
            )
        } else {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

        Log.d("location", "End location updates")
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                requestLocationAndStartUpdates()
            } else {
                Log.d("location", "Location Permission denied")
            }
        }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(key1 = true) {

        // todo: implement logic to await successful cache response
        // in the meantime: short delay
        delay(4000)
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Image(
                painter = painterResource(id = R.drawable.splashscreen_happy_bluesky),
                contentDescription = "Splash Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            }
        }
    }

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavGraph(navController: NavHostController, homeViewModel: HomeViewModel, settingsViewModel: SettingsViewModel) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable(route = "splash") {
            SplashScreen(navController = navController)
        }
        composable(route = "home") {
            AppNavigation(homeViewModel = homeViewModel, settingsViewModel = settingsViewModel)
        }
    }
}
