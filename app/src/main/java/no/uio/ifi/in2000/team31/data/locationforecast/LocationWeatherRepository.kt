package no.uio.ifi.in2000.team31.data.locationforecast

import android.util.Log
import no.uio.ifi.in2000.team31.model.WeatherData
import no.uio.ifi.in2000.team31.model.WeatherDataModel

class LocationWeatherRepository {
    private val weatherData = LocationWeatherDataSource()

    suspend fun fetchInfo(url: String): WeatherDataModel {
        Log.d("testing", "fetchInfo - Repository")
        return weatherData.fetchData(url)
    }

    suspend fun fetchWeatherData(lat: Double, lon: Double): WeatherDataModel {
        val url = "weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
        Log.d("testing", "Fething data for $lat, $lon")
        return weatherData.fetchData(url)
    }


    suspend fun getTempAndTime(lat: Double?, lon: Double?): MutableList<Map<String, Double>> {
        val url = "weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
        val weatherData = fetchInfo(url) //Lagde objekt for aa faa liste
        Log.d("fetching weatherobjects", "${weatherData.instant}")

        val temperaturesForNextHours = mutableListOf<Map<String, Double>>() //Tid og temperatur

        val numbersOfHoursToFetch = 25
        val startIndex = 1
        val endIndex = minOf(startIndex + numbersOfHoursToFetch, weatherData.instant.size)


        for (i in startIndex until endIndex) {
            val instant = weatherData.instant[i]

            val temperatureMap = mutableMapOf<String, Double>()

            instant.time?.let { time ->
                val timeParts = time.split("T")[1].split(":")
                val hour = timeParts[0]
                instant.airTemperature?.let { temperature ->
                    temperatureMap[hour] = temperature // Legg til tid og temperatur i mappe
                }
            }

            temperaturesForNextHours.add(temperatureMap) // Legg til temperatureMap i listen
        }

        Log.d("Temperatures for next hours:", "$temperaturesForNextHours")

        return temperaturesForNextHours
    }
}