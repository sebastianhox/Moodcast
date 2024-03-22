package no.uio.ifi.in2000.team31.data.locationforecast

import android.util.Log
import no.uio.ifi.in2000.team31.model.WeatherData

class LocationWeatherRepository {
    private val weatherData = LocationWeatherDataSource()

    suspend fun fetchInfo(url: String): WeatherData {
        Log.d("testing", "fetchInfo - Repository")
        return weatherData.fetchData(url)
    }

    suspend fun fetchWeatherData(lat: Double, lon: Double): WeatherData {
        val url = "weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
        Log.d("testing", "Fething data for $lat, $lon")
        return weatherData.fetchData(url)
    }
}