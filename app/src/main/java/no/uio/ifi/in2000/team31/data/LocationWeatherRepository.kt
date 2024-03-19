package no.uio.ifi.in2000.team31.data

import android.util.Log
import no.uio.ifi.in2000.team31.model.WeatherData

class LocationWeatherRepository {
    private val weatherData = LocationWeatherDataSource()

    suspend fun fetchInfo(url: String): WeatherData {
        Log.d("testing", "fetchInfo - Repository")
        return weatherData.fetchData(url)
    }
}