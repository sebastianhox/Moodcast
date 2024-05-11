package no.uio.ifi.in2000.team31.ui.alert

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.dsl.point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.MoodApplication
import no.uio.ifi.in2000.team31.cache.CachePolicy
data class WeatherAlertUIState(
    val features: List<Feature>? = listOf()
)
class AlertViewModel(application: Application) : AndroidViewModel(application) {
    private val appContainer = (application as MoodApplication).appContainer
    private val alertRepository = appContainer.alertRepository

    private val _weatherAlertUIState = MutableStateFlow(WeatherAlertUIState())
    val weatherAlertUIState: StateFlow<WeatherAlertUIState> = _weatherAlertUIState.asStateFlow()
    fun startAlertUpdates() {
        viewModelScope.launch {
            _weatherAlertUIState.update { currentState ->
                currentState.copy(
                    features = alertRepository.getDangerZonesOf(point(37.4220936, -122.083922), CachePolicy(CachePolicy.Type.ALWAYS))
                )
            }
        }
    }
}