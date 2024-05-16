package no.uio.ifi.in2000.team31

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.ui.activity.WeatherStatus
import no.uio.ifi.in2000.team31.ui.mood.Mood


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
class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val appContainer = (application as MoodApplication).appContainer
    private val connecivityObserver = appContainer.connectivityObserver

    val connectionStatus = connecivityObserver.observe()
    private val _moodUIState = MutableStateFlow(MoodUIState())
    val moodUIState: StateFlow<MoodUIState> = _moodUIState.asStateFlow()

    private val _weatherUIState = MutableStateFlow(WeatherUIState())
    val weatherUIState: StateFlow<WeatherUIState> = _weatherUIState.asStateFlow()

    private val _locationUIState = MutableStateFlow(LocationUIState())
    val locationUIState: StateFlow<LocationUIState> = _locationUIState.asStateFlow()

    init {
        viewModelScope.launch {
            connectionStatus.collect {connectionStatus
            }
        }
    }
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
                    lat = lat,
                    lon = lon
                )
            }
        }
    }

}