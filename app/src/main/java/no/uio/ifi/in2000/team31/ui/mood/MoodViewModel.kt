package no.uio.ifi.in2000.team31.ui.mood

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.container.MoodApplication
import no.uio.ifi.in2000.team31.data.network.Status
import no.uio.ifi.in2000.team31.data.cachePolicy.CachePolicy

data class MoodWeatherUIState(
    val symbolCodeNow: String? = null,
    val temperature: Double? = null,
)

class MoodViewModel(application: Application) : AndroidViewModel(application) {
    private val appContainer = (application as MoodApplication).appContainer
    private val repository = appContainer.locWeatherRepository
    private val sharedViewModel = appContainer.sharedViewModel

    private val _weatherDataUIState = MutableStateFlow(MoodWeatherUIState())
    val weatherDataUIState: StateFlow<MoodWeatherUIState> = _weatherDataUIState
    fun manuallyUpdate() {
        viewModelScope.launch {
            sharedViewModel.connectionStatus.collect { status ->
                if (status == Status.Available) {
                    val dataNow = repository.fetchInfo(
                        59.913868,
                        10.752245,
                        CachePolicy(CachePolicy.Type.ALWAYS)
                    ).instant.first()
                    _weatherDataUIState.update { currentState ->
                        currentState.copy(
                            symbolCodeNow = dataNow.symbolCode,
                            temperature = dataNow.airTemperature
                        )
                    }
                }
            }
        }
    }
}

