package no.uio.ifi.in2000.team31.data.locationforecast

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import no.uio.ifi.in2000.team31.cache.CachePolicy
import no.uio.ifi.in2000.team31.cache.CachePolicy.Type.ALWAYS
import no.uio.ifi.in2000.team31.cache.CachePolicy.Type.NEVER
import no.uio.ifi.in2000.team31.cache.CachePolicy.Type.REFRESH
import no.uio.ifi.in2000.team31.model.WeatherDataModel
import java.time.Clock
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LocationWeatherRepository(private val weatherDataSource : LocationWeatherDataSource) {
    private lateinit var cachedData: WeatherDataModel
    suspend fun fetchInfo(lat: Double?, lon: Double?, cachePolicy: CachePolicy): WeatherDataModel {
        return when (cachePolicy.type) {
            NEVER -> fetch(lat, lon)
            ALWAYS -> if (::cachedData.isInitialized) cachedData else fetch(lat, lon)
            REFRESH -> fetchAndCache(lat, lon)
            else -> fetch(lat,lon)
        }
    }

    private suspend fun fetch(lat: Double?, lon: Double?): WeatherDataModel {
        Log.d("API Request", "fetchInfo - LocWeather Repository")
        return weatherDataSource.fetchData(lat, lon)
    }

    private suspend fun fetchAndCache(lat:Double?, lon:Double?): WeatherDataModel {
        cachedData = fetch(lat, lon)
        return cachedData
    }

//    suspend fun getSymbolCodeNow(lat: Double?, lon: Double?): String? {
//        val weatherData = fetchInfo(lat, lon, CachePolicy(CachePolicy.Type.ALWAYS))
//        return weatherData.instant.first().symbolCode
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun get24HoursForecast(lat: Double?, lon: Double?): List<Triple<String?, Double?, String?>> {

        val weatherData = fetchInfo(lat, lon, CachePolicy(CachePolicy.Type.ALWAYS))
        val temperaturesForNextHours = mutableListOf<Triple<String?,Double?,String?>>() // hourly forecast

        weatherData.instant.subList(1,26).forEach{hourlyData ->

            val utcHour = hourlyData.time?.substring(11,16)
            val utcTime = LocalTime.parse(utcHour)
            val targetOffsetTime = utcTime.plusHours(2)
            val localHour = targetOffsetTime.format(DateTimeFormatter.ofPattern("HH:mm"))

            val temperature = hourlyData.airTemperature
            val symbolCode = hourlyData.symbolCode

            temperaturesForNextHours.add(Triple(localHour.substring(0,2),temperature,symbolCode))
        }

        return temperaturesForNextHours
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getNext7Days(lat: Double?, lon: Double?): Map<String, Triple<String?, Double, Double>> {

        val weatherData = fetchInfo(lat, lon, CachePolicy(CachePolicy.Type.ALWAYS))


        val longTermForecast = mutableMapOf<String, Triple<String?, Double, Double>>() // long term forecast

        val startIndeks = 1 // Startindeks for neste dag
        val endIndeks = weatherData.instant.size

        //-------------------------------------------------------------------------

        var dateToday = "" // Variabel for å holde den nåværende datoen
        var maxTemp =
            Double.MIN_VALUE // Variabel for å holde maksimumstemperaturen for den nåværende dagen
        var minTemp =
            Double.MAX_VALUE
        var symbolCode: String? = null// Variabel for å holde minimumstemperaturen for den nåværende dagen

        var currentDateIndex =
            startIndeks // Variabel for å holde indeksen til den nåværende datoen i listen

        // Loop gjennom alle tidene i listen
        while (currentDateIndex < endIndeks) {
            val dayForecast = weatherData.instant[currentDateIndex]

            dayForecast.time?.let { time ->
                val day = time.split("T")[0] // Hent ut datoen for langtidsvarselet
                val givenTime = time.split("T")[1]
                // Sjekk om `day` er lik `dateToday`
                if (day == dateToday) {
                    // Sjekk om temperaturen er høyere eller lavere enn min og maks for nåværende dag
                    dayForecast.airTemperature?.let { temperature ->
                        maxTemp = maxOf(maxTemp, temperature) // Oppdater maksimumstemperaturen
                        minTemp = minOf(minTemp, temperature) // Oppdater minimumstemperaturen
                    }
                    if (givenTime == "12:00:00Z" || day == LocalDate.now().toString()) {
                        symbolCode = dayForecast.symbolCode ?: dayForecast.next6Hours_symbolCode
                    }

                } else {
//
                    longTermForecast[dateToday] = Triple(symbolCode, minTemp, maxTemp)

                    // Hvis `day` er forskjellig fra `dateToday`, legg til temperaturene for forrige dag i forecasten
//                    longTermForecast[dateToday] = Pair(minTemp, maxTemp)

                    // Oppdater `dateToday` til den nåværende datoen
                    dateToday = day
                    Log.d("test", "$maxTemp  $minTemp")
                    // Nullstill maksimums- og minimumstemperaturen for den nye dagen
                    maxTemp = Double.MIN_VALUE
                    minTemp = Double.MAX_VALUE
                    symbolCode = null
                }

            }

            // Hopp til neste tid i listen
            currentDateIndex += 1
        }

        return longTermForecast
    }
}