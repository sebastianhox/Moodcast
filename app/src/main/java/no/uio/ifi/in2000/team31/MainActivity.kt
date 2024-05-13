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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.team31.cache.CachePolicy
import no.uio.ifi.in2000.team31.ui.home.HomeViewModel
import no.uio.ifi.in2000.team31.ui.navigation.AppNavigation
import no.uio.ifi.in2000.team31.ui.settings.SettingsViewModel
import no.uio.ifi.in2000.team31.ui.theme.Team31Theme
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var appContainer: AppContainer
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var settingsViewModel: SettingsViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private var mInstanceActivity: WeakReference<MainActivity>? = null
        fun getInstance(): MainActivity? {
            return mInstanceActivity?.get()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("location", "onCreate")

        appContainer = (application as MoodApplication).appContainer
        sharedViewModel = appContainer.sharedViewModel
        settingsViewModel = appContainer.settingsViewModel
        mInstanceActivity = WeakReference(this) // set the instance in order to call functions from other classes

        setContent {
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()


            Team31Theme(isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(homeViewModel = homeViewModel)

                }

            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationAndStartUpdates(CachePolicy(CachePolicy.Type.ALWAYS))
    }

    //    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onResume() {
//        super.onResume()
//        Log.d("location", "onResume")
//
//    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun requestLocationAndStartUpdates(cachePolicy: CachePolicy = CachePolicy(CachePolicy.Type.NEVER)) {
        Log.d("location", "Start location permission request / updates")

        val locationOn = settingsViewModel.locationOn.value

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                fusedLocationClient.removeLocationUpdates(this)
                if (locationResult.locations.isNotEmpty()) {
                    val newLocation = locationResult.locations[0]
                    homeViewModel.fetchWeatherData(
                        newLocation.latitude,
                        newLocation.longitude,
                        cachePolicy
                    )
                    sharedViewModel.updateLocation(newLocation.latitude, newLocation.longitude)

                } else {
                    Log.w("location", "Error with fetching the location")
                }
            }
        }

        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION

            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("location", "Requesting permissions")
            requestPermissionLauncher.launch(permissions)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location == null) {
                    Log.d("location", "Trying to refetch")
                    fusedLocationClient.requestLocationUpdates(
                        LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, 2000)
                            .build(),
                        locationCallback,
                        null
                    )
                } else {
                    Log.d(
                        "location",
                        "Fetched location: ${location.latitude}, ${location.longitude}, $cachePolicy"
                    )
                    homeViewModel.fetchWeatherData(
                        location.latitude,
                        location.longitude,
                        cachePolicy
                    )
                    sharedViewModel.updateLocation(location.latitude, location.longitude)
                }
            }
        }
        Log.d("location", "End location updates")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    settingsViewModel.onLocationSwitchChange(true)
//                    requestLocationAndStartUpdates()
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    settingsViewModel.onLocationSwitchChange(true)
//                    requestLocationAndStartUpdates()
                }

                else -> {
                    Log.d("location", "Location Permission denied")
                    settingsViewModel.onLocationSwitchChange(false)
                    homeViewModel.fetchWeatherData(
                        59.913868,
                        10.752245,
                        CachePolicy(CachePolicy.Type.NEVER)
                    )
                }
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


//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun SetupNavGraph(navController: NavHostController, homeViewModel: HomeViewModel, settingsViewModel: SettingsViewModel) {
//    NavHost(
//        navController = navController,
//        startDestination = "splash"
//    ) {
//        composable(route = "splash") {
//            SplashScreen(navController = navController)
//        }
//        composable(route = "home") {
//            AppNavigation(homeViewModel = homeViewModel)
//        }
//    }
//}
