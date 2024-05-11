package no.uio.ifi.in2000.team31

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.ui.mood.Mood

data class MoodUIState(
    val selectedMood: Mood? = null
)

data class LocationUIState(
    val lat: Double? = null ,
    val lon: Double? = null
)
class SharedViewModel : ViewModel() {
    private val _moodUIState = MutableStateFlow(MoodUIState())
    val moodUIState: StateFlow<MoodUIState> = _moodUIState.asStateFlow()

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

    fun updateLocation(lat: Double?, lon: Double?) {
        viewModelScope.launch {
            _locationUIState.update { currentState ->
                currentState.copy(
                    lat = lat,
                    lon = lon
                )
            }
        }
    }

}