package no.uio.ifi.in2000.team31

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.team31.cache.CachePolicy
import no.uio.ifi.in2000.team31.data.settings.SettingsRepository
import no.uio.ifi.in2000.team31.ui.home.HomeViewModel
import no.uio.ifi.in2000.team31.ui.navigation.AppNavigation
import no.uio.ifi.in2000.team31.ui.settings.SettingsViewModel
import no.uio.ifi.in2000.team31.ui.theme.Team31Theme

class MainActivity : ComponentActivity() {


    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var appContainer: AppContainer
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var settingsViewModel: SettingsViewModel


    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("location", "onCreate")


        appContainer = (application as MoodApplication).appContainer
        sharedViewModel = appContainer.sharedViewModel
        val settingsRepository = SettingsRepository(applicationContext)
        settingsViewModel = SettingsViewModel(settingsRepository)

        setContent {
            Team31Theme(settingsViewModel.isDarkTheme.collectAsState().value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(homeViewModel = homeViewModel, settingsViewModel = settingsViewModel)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        val appContainer = (application as MoodApplication).appContainer
        val sharedViewModel = appContainer.sharedViewModel

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationAndStartUpdates(CachePolicy(CachePolicy.Type.ALWAYS))
        Log.d("location", "onResume")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestLocationAndStartUpdates(cachePolicy: CachePolicy) {
        Log.d("location", "Start location permission request / updates")

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                fusedLocationClient.removeLocationUpdates(this)
                if (locationResult.locations.isNotEmpty()) {
                    val newLocation = locationResult.locations[0]
                    homeViewModel.fetchWeatherData(newLocation.latitude,newLocation.longitude, cachePolicy)
                    sharedViewModel.updateLocation(newLocation.latitude, newLocation.longitude)

                } else {
                    Log.w("location", "Error with fetching the location")
                }
            }
        }

        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("location", "Requesting permissions")
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    requestLocationAndStartUpdates(cachePolicy)
                } else {
                    Log.d("location", "Location Permission denied")
                }
            }.launch(
                permission
            )
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location == null) {
                    Log.d("location", "Trying to refetch")
                    fusedLocationClient.requestLocationUpdates(
                        LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 2000).build(),
                        locationCallback,
                        null)
                } else {
                    Log.d("location", "Fetched location: ${location.latitude}, ${location.longitude}")
                    homeViewModel.fetchWeatherData(location.latitude,location.longitude, cachePolicy)
                    sharedViewModel.updateLocation(location.latitude, location.longitude)
                }
            }
        }
        Log.d("location","End location updates")
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted: Boolean ->
//            if (isGranted) {
//                requestLocationAndStartUpdates(sharedViewModel)
//            } else {
//                Log.d("location", "Location Permission denied")
//            }
//        }
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
