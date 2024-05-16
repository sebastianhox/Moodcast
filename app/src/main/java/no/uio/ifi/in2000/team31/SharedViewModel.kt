package no.uio.ifi.in2000.team31

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.ui.activity.WeatherStatus
import no.uio.ifi.in2000.team31.ui.mood.Mood

fun getWeatherStatus(symbolCode: String?): WeatherStatus {
    return when {
        symbolCode?.startsWith("clearsky") == true -> WeatherStatus.SUNNY
        symbolCode?.startsWith("fair") == true -> WeatherStatus.SUNNY
        symbolCode?.startsWith("partlycloudy") == true -> WeatherStatus.CLOUDY
        symbolCode?.startsWith("cloudy") == true -> WeatherStatus.CLOUDY
        symbolCode?.contains("rain") == true -> WeatherStatus.RAINY
        symbolCode?.contains("sleet") == true -> WeatherStatus.RAINY
        else -> WeatherStatus.CLOUDY // Default to cloudy if not matched
    }
}

data class MoodUIState(
    val selectedMood: Mood? = null
)

data class WeatherUIState(
    val currentWeatherStatus: WeatherStatus? = null
)

data class LocationUIState(
    val lat: Double? = null ,
    val lon: Double? = null
)
class SharedViewModel : ViewModel() {
    private val _moodUIState = MutableStateFlow(MoodUIState())
    val moodUIState: StateFlow<MoodUIState> = _moodUIState.asStateFlow()

    private val _weatherUIState = MutableStateFlow(WeatherUIState())
    val weatherUIState: StateFlow<WeatherUIState> = _weatherUIState.asStateFlow()

    private val _locationUIState = MutableStateFlow(LocationUIState())
    val locationUIState: StateFlow<LocationUIState> = _locationUIState.asStateFlow()
    fun setSelectedMood(mood: Mood?) {
        viewModelScope.launch {
            _moodUIState.update { currentState ->
                currentState.copy(
                    selectedMood = mood
                )
            }
        }
    }

    fun setCurrentWeatherStatus(weatherStatus: WeatherStatus?) {
        viewModelScope.launch {
            _weatherUIState.update { currentState ->
                currentState.copy(
                    currentWeatherStatus = weatherStatus
                )
            }
        }
    }

    fun updateLocation(lat: Double?, lon: Double?) {
        viewModelScope.launch {
            _locationUIState.update { currentState ->
                Log.d("refreshstuff", "lat: $lat, lon: $lon")
                currentState.copy(
                    lat = lat ?: currentState.lat,
                    lon = lon ?: currentState.lon
                )
            }
        }
    }

}