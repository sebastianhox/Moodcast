package no.uio.ifi.in2000.team31.data.weatherAlert

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.team31.model.WeatherAlert
import no.uio.ifi.in2000.team31.model.WeatherAlertModel

class WeatherAlertsDataSource {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
        defaultRequest {
            url("https://gw-uio.intark.uh-it.no/in2000/")
            header("X-Gravitee-API-Key", "0cdacead-89b3-408a-bcf5-bb6bd685a874")
        }
    }

    suspend fun fetchData(url: String) {
        val response: HttpResponse = client.get(url)
        Log.d("apiReq", response.status.value.toString())

        val alert:WeatherAlert = response.body()
    }
}