package no.uio.ifi.in2000.team31.data.geoname

import no.uio.ifi.in2000.team31.model.GeonameData
import no.uio.ifi.in2000.team31.model.GeonamesModel

class GeonameRepository(private val dataSource: GeonameDataSource) {

    suspend fun getPlaceRecommendations(query: String): GeonamesModel {
        return dataSource.fetchPlaces(query)
    }
}