package no.uio.ifi.in2000.team31.ui.home

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.dsl.point
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.data.locationforecast.LocationWeatherRepository
import no.uio.ifi.in2000.team31.data.weatheralert.WeatherAlertRepository
import no.uio.ifi.in2000.team31.model.WeatherData
import no.uio.ifi.in2000.team31.model.WeatherDataModel

data class WeatherDataUIState(
    val weatherData: WeatherDataModel? = null,
    val tempAndTimeData: MutableList<Map<String, Double>>? = null
)

data class WeatherAlertUIState(
    val features: List<io.github.dellisd.spatialk.geojson.Feature> = listOf()
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private val repository = LocationWeatherRepository()

    private val alertRepository: WeatherAlertRepository = WeatherAlertRepository()

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted.asStateFlow()

    private val _locationState = MutableStateFlow(Pair(0.0, 0.0))
    val locationState: StateFlow<Pair<Double, Double>> = _locationState.asStateFlow()

    private val _weatherDataUIState = MutableStateFlow(WeatherDataUIState())
    val weatherDataUIState: StateFlow<WeatherDataUIState> = _weatherDataUIState.asStateFlow()

    private val _weatherAlertUIState = MutableStateFlow(WeatherAlertUIState())
    val weatherAlertUIState: StateFlow<WeatherAlertUIState> = _weatherAlertUIState.asStateFlow()

    fun updatePermissionStatus(isGranted: Boolean) {
        _permissionGranted.value = isGranted
        if (isGranted) startLocationUpdates()
    }

    fun checkPermissionsAndStartUpdates(context: Context) {
        val permission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            _permissionGranted.value = true
            startLocationUpdates()
            startAlertUpdates()
        } else {
            Log.d("testing", "No permissions")
        }
    }

    fun startAlertUpdates(point: Point = point(6.352810,59.650822) ) {
        viewModelScope.launch {
            _weatherAlertUIState.update { currentState ->
                currentState.copy(
                    features = alertRepository.getDangerZonesOf(point)
                )
            }
        }
    }

    fun startLocationUpdates() {
        val permission = ContextCompat.checkSelfPermission(
            getApplication(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest.Builder(1000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateIntervalMillis(5000)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let {
                        _locationState.value = Pair(it.latitude, it.longitude)
                        fetchWeatherData(it.latitude, it.longitude)
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
            )
        }
    }

    private fun fetchWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                /*
                val weatherData: WeatherDataModel = repository.fetchWeatherData(latitude, longitude)
                _weatherDataUIState.value = weatherData*/
                val url = "weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
                _weatherDataUIState.update { currentState ->
                    currentState.copy(
                        weatherData = repository.fetchInfo(url),
                        tempAndTimeData = repository.getTempAndTime(lat, lon)
                    )
                }

            } catch (e: Exception) {
                Log.e("testing", "Error fetching weather data", e)
            }
        }
    }
}