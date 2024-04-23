package no.uio.ifi.in2000.team31.ui.home

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.dsl.point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.MyApplication
import no.uio.ifi.in2000.team31.data.locationforecast.LocationWeatherRepository
import no.uio.ifi.in2000.team31.data.weatheralert.WeatherAlertRepository
import no.uio.ifi.in2000.team31.model.WeatherDataModel

data class WeatherDataUIState(
    val weatherData: WeatherDataModel? = null,
    val tempAndTimeData: MutableList<Map<String, Double>>? = null,
    val longTermForecast: Map<String, Pair<Double, Double>>? = null,
    val features: List<Feature> = listOf()
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val appContainer = (application as MyApplication).appContainer
    private val repository = appContainer.locWeatherRepository
    private val alertRepository = appContainer.alertRepository

    private val _weatherDataUIState = MutableStateFlow(WeatherDataUIState())
    val weatherDataUIState: StateFlow<WeatherDataUIState> = _weatherDataUIState.asStateFlow()

    fun fetchWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val url = "weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
                _weatherDataUIState.update { currentState ->
                    currentState.copy(
                        weatherData = repository.fetchInfo(url),
                        tempAndTimeData = repository.getNext24Hours(lat, lon),
                        longTermForecast = repository.getNext7Days(lat,lon),
                        features = alertRepository.getDangerZonesOf(point(lat,lon))
                    )
                }

            } catch (e: Exception) {
                Log.e("testing", "Error fetching weather data", e)
            }
        }
    }
}