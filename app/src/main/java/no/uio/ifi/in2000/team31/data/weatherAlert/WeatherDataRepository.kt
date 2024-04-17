package no.uio.ifi.in2000.team31.data.weatherAlert

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import no.uio.ifi.in2000.team31.model.WeatherDataInstant
import no.uio.ifi.in2000.team31.model.WeatherDataModel
import no.uio.ifi.in2000.team31.model.toModelInstant
import java.time.LocalDateTime

class WeatherDataRepository {
    private val weatherData = WeatherDataDataSource()

    suspend fun fetchInfo(url: String): WeatherDataModel {

        return weatherData.fetchData(url).toModelInstant()
    }


    //Metode for aa hente spesifikk time av Timeseries/WeatherDataInstant liste
    //Metode for aa hente vaer info de neste x dagene

    suspend fun getTempAndTime(): MutableList<Map<String, Double>> {
        val weatherData = fetchInfo("weatherapi/locationforecast/2.0/compact?lat=60.10&lon=9.58") //Lagde objekt for aa faa liste
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