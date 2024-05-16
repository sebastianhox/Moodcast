package no.uio.ifi.in2000.team31

import no.uio.ifi.in2000.team31.model.getWeatherStatus
import no.uio.ifi.in2000.team31.ui.activity.WeatherStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherStatusTests {
    @Test
    fun testSunnyWeather() {
        assertEquals(WeatherStatus.SUNNY, getWeatherStatus("clearsky_day"))
        assertEquals(WeatherStatus.SUNNY, getWeatherStatus("clearsky_night"))
        assertEquals(WeatherStatus.SUNNY, getWeatherStatus("fair_day"))
        assertEquals(WeatherStatus.SUNNY, getWeatherStatus("fair_night"))
    }

    @Test
    fun testCloudyWeather() {
        assertEquals(WeatherStatus.CLOUDY, getWeatherStatus("partlycloudy_day"))
        assertEquals(WeatherStatus.CLOUDY, getWeatherStatus("partlycloudy_night"))
        assertEquals(WeatherStatus.CLOUDY, getWeatherStatus("cloudy"))
    }

    @Test
    fun testRainyWeather() {
        assertEquals(WeatherStatus.RAINY, getWeatherStatus("lightrain"))
        assertEquals(WeatherStatus.RAINY, getWeatherStatus("rain"))
        assertEquals(WeatherStatus.RAINY, getWeatherStatus("heavyrain"))
        assertEquals(WeatherStatus.RAINY, getWeatherStatus("lightsleet"))
        assertEquals(WeatherStatus.RAINY, getWeatherStatus("sleet"))
    }

    @Test
    fun testNullAndUnknownWeather() {
        assertEquals(WeatherStatus.CLOUDY, getWeatherStatus(null)) // Null input
        assertEquals(WeatherStatus.CLOUDY, getWeatherStatus("fog"))  // Unknown symbol
    }
}