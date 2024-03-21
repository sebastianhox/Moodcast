package no.uio.ifi.in2000.team31.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.data.LocationWeatherRepository
import no.uio.ifi.in2000.team31.data.WeatherAlertRepository
import no.uio.ifi.in2000.team31.model.WeatherData

data class WeatherUIState(
    val weatherData: WeatherData? = null,
    val lat: Double? = null,
    val lon: Double? = null
)
class HomeViewModel: ViewModel() {
    private val repository: LocationWeatherRepository = LocationWeatherRepository()
    private val _weatherUIState = MutableStateFlow(WeatherUIState())
    val weatherUIState: StateFlow<WeatherUIState> = _weatherUIState.asStateFlow()
    //private val alertRepository: WeatherAlertRepository = WeatherAlertRepository()



    fun updateWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val weatherData = repository.fetchWeatherData(lat, lon)
                _weatherUIState.update { it.copy(weatherData = weatherData, lat = lat, lon = lon) }
            } catch (e: Exception) {
                Log.e("testing", "Error fetching weather data", e)
            }
        }
    }




    /*init {
        Log.d("testing", "Før init")
        loadData("weatherapi/locationforecast/2.0/compact?lat=60.10&lon=9.58")
        Log.d("testing", "Etter init")
    }*/

    /*private fun loadData(url: String) {
        viewModelScope.launch {
            try {
                Log.d("testing", "Start loadData")
                _weatherUIState.update { currentState ->
                    currentState.copy(
                        weatherData = repository.fetchInfo(url)
                    )
                }
                Log.d("testing", "Slutt loadData")

            } catch (e: Exception) {
                Log.d("testing", "Feil ved henting av værdata")
                Log.d("testing", "Unntak: ${e.message}")
            }
        }
    }*/
}