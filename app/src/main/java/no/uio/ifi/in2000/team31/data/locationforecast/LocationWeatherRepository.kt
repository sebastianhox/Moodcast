package no.uio.ifi.in2000.team31.data.locationforecast

import android.util.Log
import no.uio.ifi.in2000.team31.model.WeatherDataInstant
import no.uio.ifi.in2000.team31.model.WeatherDataModel

class LocationWeatherRepository(private val weatherData : LocationWeatherDataSource) {

    private lateinit var cachedData: WeatherDataModel
    suspend fun fetchInfo(url: String): WeatherDataModel {
        Log.d("testing", "fetchInfo - Repository")
        val fetchedData = weatherData.fetchData(url)
        cachedData = fetchedData
        return fetchedData
    }

    /*suspend fun fetchWeatherData(lat: Double, lon: Double): WeatherDataModel {
        val url = "weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
        Log.d("testing", "Fething data for $lat, $lon")
        return weatherData.fetchData(url)
    }*/


    suspend fun getNext24Hours(lat: Double?, lon: Double?): MutableList<Map<String, Double>> {
        val url = "weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
        val weatherData = fetchInfo(url) //Lagde objekt for aa faa liste
        Log.d("fetching weatherobjects", "${weatherData}")

        val temperaturesForNextHours = mutableListOf<Map<String, Double>>() // hourly forecast

        val numbersOfHoursToFetch = 25 //Hours in a day + 1
        val startIndex = 3 //Summertime hours
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

    suspend fun getNext7Days(lat: Double?, lon: Double?): Map<String, Pair<Double, Double>> {
        val url = "weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
        val weatherData = fetchInfo(url) // api call to retrieve weather objects
        val weatherInstant = weatherData.instant

        val longTermForecast = mutableMapOf<String, Pair<Double, Double>>() // long term forecast

        val numberOfDaysToFetch = weatherInstant.size

        Log.d("size:", "$numberOfDaysToFetch") //gir 88/87

        val startIndeks = 1 // Startindeks for neste dag
        val endIndeks = minOf(
            startIndeks + numberOfDaysToFetch * 24,
            weatherInstant.size
        ) // Hent 7 dager med data

        //-------------------------------------------------------------------------

        var dateToday = "" // Variabel for å holde den nåværende datoen
        var maxTemp =
            Double.MIN_VALUE // Variabel for å holde maksimumstemperaturen for den nåværende dagen
        var minTemp =
            Double.MAX_VALUE // Variabel for å holde minimumstemperaturen for den nåværende dagen

        var currentDateIndex =
            startIndeks // Variabel for å holde indeksen til den nåværende datoen i listen

        // Loop gjennom alle tidene i listen
        while (currentDateIndex < endIndeks) {
            val dayForecast = weatherInstant[currentDateIndex]

            dayForecast.time?.let { time ->
                val day = time.split("T")[0] // Hent ut datoen for langtidsvarselet

                // Sjekk om `day` er lik `dateToday`
                if (day == dateToday) {
                    // Sjekk om temperaturen er høyere eller lavere enn min og maks for nåværende dag
                    dayForecast.airTemperature?.let { temperature ->
                        maxTemp = maxOf(maxTemp, temperature) // Oppdater maksimumstemperaturen
                        minTemp = minOf(minTemp, temperature) // Oppdater minimumstemperaturen
                    }
                } else {
                    // Hvis `day` er forskjellig fra `dateToday`, legg til temperaturene for forrige dag i forecasten
                    longTermForecast[dateToday] = Pair(minTemp, maxTemp)

                    // Oppdater `dateToday` til den nåværende datoen
                    dateToday = day

                    // Nullstill maksimums- og minimumstemperaturen for den nye dagen
                    maxTemp = Double.MIN_VALUE
                    minTemp = Double.MAX_VALUE
                }
            }

            // Hopp til neste tid i listen
            currentDateIndex += 1
        }

        Log.d("Langtidsvarsel:", "$longTermForecast")

        return longTermForecast
    }

    fun getCachedData(): WeatherDataModel? {
        return if (::cachedData.isInitialized) {
            cachedData
        } else {
            null
        }
    }
}