package no.uio.ifi.in2000.team31.data

import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Geometry
import io.github.dellisd.spatialk.geojson.MultiPolygon
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Polygon
import io.github.dellisd.spatialk.turf.booleanPointInPolygon

class WeatherAlertRepository {
    private val alert = WeatherAlertDataSource()

    suspend fun getDangerZonesOf(point: Point): List<Feature> {
        val data = alert.fetchData()
        val features = mutableListOf<Feature>()
        for (feature in data) {
            if (isInsideDangerZone(feature.geometry, point)) {
                features.add(feature)
            }
        }
        return features
    }

    /*
    Help-function used to smart-cast spatialk.Geometry class to Polygon/MultiPolygon class
    */
    private fun isInsideDangerZone(polygon: Geometry?, point: Point): Boolean {
        when (polygon) {
            is Polygon -> {
                if (booleanPointInPolygon(point, polygon)) {
                    return true
                }
            }
            is MultiPolygon -> {
                if (booleanPointInPolygon(point, polygon)) {
                    return true
                }
            }
            else -> {}
        }
        return false
    }
}