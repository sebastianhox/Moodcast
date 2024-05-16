package no.uio.ifi.in2000.team31

import no.uio.ifi.in2000.team31.ui.activity.WeatherStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherStatusTests {
    @Test
    fun testSunnyWeather() {
        assertEquals(WeatherStatus.SOL, getWeatherStatus("clearsky_day"))
        assertEquals(WeatherStatus.SOL, getWeatherStatus("clearsky_night"))
        assertEquals(WeatherStatus.SOL, getWeatherStatus("fair_day"))
        assertEquals(WeatherStatus.SOL, getWeatherStatus("fair_night"))
    }

    @Test
    fun testCloudyWeather() {
        assertEquals(WeatherStatus.SKYER, getWeatherStatus("partlycloudy_day"))
        assertEquals(WeatherStatus.SKYER, getWeatherStatus("partlycloudy_night"))
        assertEquals(WeatherStatus.SKYER, getWeatherStatus("cloudy"))
    }

    @Test
    fun testRainyWeather() {
        assertEquals(WeatherStatus.REGN, getWeatherStatus("lightrain"))
        assertEquals(WeatherStatus.REGN, getWeatherStatus("rain"))
        assertEquals(WeatherStatus.REGN, getWeatherStatus("heavyrain"))
        assertEquals(WeatherStatus.REGN, getWeatherStatus("lightsleet"))
        assertEquals(WeatherStatus.REGN, getWeatherStatus("sleet"))
    }

    @Test
    fun testNullAndUnknownWeather() {
        assertEquals(WeatherStatus.SKYER, getWeatherStatus(null)) // Null input
        assertEquals(WeatherStatus.SKYER, getWeatherStatus("fog"))  // Unknown symbol
    }
}