package no.uio.ifi.in2000.team31.data.weatheralert

import io.github.dellisd.spatialk.geojson.FeatureCollection
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.gson.gson

class WeatherAlertDataSource {
    private val client = HttpClient() {
        install(ContentNegotiation) {
            gson()
        }
        defaultRequest {
            url("https://gw-uio.intark.uh-it.no/in2000/")
            header("X-Gravitee-API-Key", "0cdacead-89b3-408a-bcf5-bb6bd685a874")
        }
    }
    suspend fun fetchData(): FeatureCollection {
        val response: HttpResponse = client.get("weatherapi/metalerts/2.0/current.json")
        return FeatureCollection.fromJson(response.bodyAsText())
    }
}