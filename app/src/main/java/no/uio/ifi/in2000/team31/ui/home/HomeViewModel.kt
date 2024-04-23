package no.uio.ifi.in2000.team31.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.dsl.point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.MyApplication
import no.uio.ifi.in2000.team31.cache.CachePolicy
import no.uio.ifi.in2000.team31.model.WeatherDataModel

data class WeatherDataUIState(
    val weatherData: WeatherDataModel? = null,
    val tempAndTimeData: MutableList<Map<String, Double>>? = null,
    val longTermForecast: Map<String, Pair<Double, Double>>? = null,
    val features: List<Feature>? = listOf()
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
                _weatherDataUIState.update { currentState ->
                    currentState.copy(
                        weatherData = repository.fetchInfo(lat, lon, CachePolicy(CachePolicy.Type.REFRESH)),
                        tempAndTimeData = repository.getNext24Hours(lat, lon),
                        longTermForecast = repository.getNext7Days(lat,lon),
                        features = alertRepository.getDangerZonesOf(point(lon,lat), CachePolicy(CachePolicy.Type.REFRESH))
                    )
                }

            } catch (e: Exception) {
                Log.e("testing", "Error fetching weather data", e)
            }
        }
    }
}