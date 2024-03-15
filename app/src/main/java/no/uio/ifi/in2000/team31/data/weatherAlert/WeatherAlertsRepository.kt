package no.uio.ifi.in2000.team31.data.weatherAlert

import no.uio.ifi.in2000.team31.model.WeatherAlert

class WeatherAlertsRepository {
    private val weatherAlert = WeatherAlertsDataSource()

    suspend fun fetchInfo(url: String): WeatherAlert {

        return weatherAlert.fetchData(url)
    }

}