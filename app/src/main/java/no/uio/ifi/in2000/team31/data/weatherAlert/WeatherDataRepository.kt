package no.uio.ifi.in2000.team31.data.weatherAlert

import no.uio.ifi.in2000.team31.model.WeatherDataModel
import no.uio.ifi.in2000.team31.model.toModelInstant

class WeatherDataRepository {
    private val weatherData = WeatherDataDataSource()

    suspend fun fetchInfo(url: String): WeatherDataModel {

        return weatherData.fetchData(url).toModelInstant()
    }

}