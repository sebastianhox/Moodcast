package no.uio.ifi.in2000.team31.data.geoname

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.team31.model.GeonameDTO
import no.uio.ifi.in2000.team31.model.GeonamesModel
import no.uio.ifi.in2000.team31.model.toModel

class GeonameDataSource {
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun fetchPlaces(query: String): GeonamesModel {
        val response =
            client.get("http://api.geonames.org/searchJSON?name=$query&name_startsWith=${query.first()}&featureClass=A&featureClass=P&countryBias=NO&lang=NO&maxRows=5&username=sebastian_uio")
        return response.body<GeonameDTO>().toModel()
    }
}