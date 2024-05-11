package no.uio.ifi.in2000.team31.ui.mood

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.MoodApplication
import no.uio.ifi.in2000.team31.cache.CachePolicy
import no.uio.ifi.in2000.team31.ui.home.WeatherDataUIState

data class MoodWeatherUIState(
    val symbolCodeNow: String? = null,
    val temperature: Double? = null
)
class MoodViewModel(application: Application): AndroidViewModel(application) {
    private val appContainer = (application as MoodApplication).appContainer
    private val repository = appContainer.locWeatherRepository

    private val _weatherDataUIState = MutableStateFlow(MoodWeatherUIState())
    val weatherDataUIState: StateFlow<MoodWeatherUIState> = _weatherDataUIState

     init {
        viewModelScope.launch {
            val dataNow = repository.fetchInfo(null,null,CachePolicy(CachePolicy.Type.ALWAYS)).instant.first()
            _weatherDataUIState.update { currentState ->
                currentState.copy(
                    symbolCodeNow = dataNow.symbolCode,
                    temperature = dataNow.airTemperature
                )
            }
        }
    }

    fun manuallyUpdate() {
        viewModelScope.launch {
            val dataNow = repository.fetchInfo(null,null,CachePolicy(CachePolicy.Type.ALWAYS)).instant.first()
            _weatherDataUIState.update { currentState ->
                currentState.copy(
                    symbolCodeNow = dataNow.symbolCode,
                    temperature = dataNow.airTemperature
                )
            }
        }
    }
}

