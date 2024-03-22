package no.uio.ifi.in2000.team31.ui.alert

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.dsl.point
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.data.weatheralert.WeatherAlertRepository
import no.uio.ifi.in2000.team31.model.WeatherData
import no.uio.ifi.in2000.team31.ui.home.WeatherAlertUIState


data class WeatherAlertUIState(
    val features: List<io.github.dellisd.spatialk.geojson.Feature> = listOf()
)

class AlertViewModel(application: Application) : AndroidViewModel(application) {
    private val alertRepository: WeatherAlertRepository = WeatherAlertRepository()

    private val _weatherData = MutableStateFlow<WeatherData?>(null)
    val weatherData: StateFlow<WeatherData?> = _weatherData.asStateFlow()

    private val _weatherAlertUIState = MutableStateFlow(WeatherAlertUIState())
    val weatherAlertUIState: StateFlow<WeatherAlertUIState> = _weatherAlertUIState.asStateFlow()

    fun checkPermissionsAndStartUpdates(context: Context) {
        val permission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startAlertUpdates()
        } else {
            Log.d("testing", "No permissions")
        }
    }

    private fun startAlertUpdates(point: Point = point(6.352810,59.650822) ) {
        viewModelScope.launch {
            _weatherAlertUIState.update { currentState ->
                currentState.copy(
                    features = alertRepository.getDangerZonesOf(point)
                )
            }
        }
    }
}