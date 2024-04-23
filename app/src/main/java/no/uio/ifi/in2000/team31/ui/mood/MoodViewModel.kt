package no.uio.ifi.in2000.team31.ui.mood

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.MyApplication
import no.uio.ifi.in2000.team31.cache.CachePolicy
import no.uio.ifi.in2000.team31.ui.home.WeatherDataUIState

class MoodViewModel(application: Application): AndroidViewModel(application) {
    private val appContainer = (application as MyApplication).appContainer
    private val repository = appContainer.locWeatherRepository

    private val _weatherDataUIState = MutableStateFlow(WeatherDataUIState())
    val weatherDataUIState: StateFlow<WeatherDataUIState> = _weatherDataUIState

    init {
        viewModelScope.launch {
            _weatherDataUIState.update { currentState ->
                currentState.copy(
                    weatherData = repository.fetchInfo(null,null,cachePolicy = CachePolicy(CachePolicy.Type.ALWAYS))
                )
            }
        }
    }
}

