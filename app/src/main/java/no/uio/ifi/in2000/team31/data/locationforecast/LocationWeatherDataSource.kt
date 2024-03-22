package no.uio.ifi.in2000.team31.data.locationforecast

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import io.ktor.util.appendIfNameAbsent
import no.uio.ifi.in2000.team31.model.WeatherData
import no.uio.ifi.in2000.team31.model.WeatherDataModel
import no.uio.ifi.in2000.team31.model.toModelInstant

class LocationWeatherDataSource {

    private val client = HttpClient() {
        install(ContentNegotiation) {
            gson()
        }
        defaultRequest {
            url("https://gw-uio.intark.uh-it.no/in2000/")
            headers.appendIfNameAbsent("X-Gravitee-API-Key", "0cdacead-89b3-408a-bcf5-bb6bd685a874")
        }
    }

    suspend fun fetchData(url: String): WeatherDataModel {
        Log.d("testing", "Før client.get(url) - DataSource")
        val response: HttpResponse = client.get(url)
        Log.d("testing", "Etter client.get(url) - DataSource")
        return response.body<WeatherData>().toModelInstant()
    }
}