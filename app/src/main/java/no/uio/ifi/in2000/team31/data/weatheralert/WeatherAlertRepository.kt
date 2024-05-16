package no.uio.ifi.in2000.team31.data.weatheralert

import android.util.Log
import io.github.dellisd.spatialk.geojson.Feature
import io.github.dellisd.spatialk.geojson.Geometry
import io.github.dellisd.spatialk.geojson.MultiPolygon
import io.github.dellisd.spatialk.geojson.Point
import io.github.dellisd.spatialk.geojson.Polygon
import io.github.dellisd.spatialk.geojson.dsl.point
import io.github.dellisd.spatialk.turf.booleanPointInPolygon
import no.uio.ifi.in2000.team31.cache.CachePolicy
import no.uio.ifi.in2000.team31.cache.CachePolicy.Type.NEVER
import no.uio.ifi.in2000.team31.cache.CachePolicy.Type.ALWAYS
import no.uio.ifi.in2000.team31.cache.CachePolicy.Type.REFRESH


class WeatherAlertRepository(private val alert : WeatherAlertDataSource) {
    private lateinit var featuresCached: List<Feature>

    suspend fun getDangerZonesOf(point: Point, cachePolicy: CachePolicy): List<Feature>? {
        return when (cachePolicy.type) {
            NEVER -> fetch(point)
            ALWAYS -> featuresCached
            REFRESH -> fetchAndCache(point)
            else -> null
        }
    }

    suspend fun getAlertIcons(lat: Double?, lon: Double?, cachePolicy: CachePolicy): List<Pair<String?,String?>> {
        val point = if (lat == null || lon == null) {
            point(59.914099, 10.750554)
        } else {
            point(lon, lat)
        }
        val features = when (cachePolicy.type) {
            NEVER -> fetch(point)
            ALWAYS -> featuresCached
            REFRESH -> fetchAndCache(point)
            else -> null
        }

        val alertIcons = mutableListOf<Pair<String?,String?>>()
        features?.forEach {feature ->
            val event = feature.getStringProperty("event")
            val color = feature.getStringProperty("riskMatrixColor")
            alertIcons.add(Pair(event,color))
        }

        return alertIcons
    }

    private suspend fun fetch(point: Point): List<Feature> {
        Log.d("API Request", "fetch alert data remotely - Alert Repository")
        val data = alert.fetchData()
        val features = mutableListOf<Feature>()
        for (feature in data) {
            if (isInsideDangerZone(feature.geometry, point)) {
                features.add(feature)
            }
        }
        return features
    }

    private suspend fun fetchAndCache(point: Point): List<Feature> {
        featuresCached = fetch(point)
        return featuresCached
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