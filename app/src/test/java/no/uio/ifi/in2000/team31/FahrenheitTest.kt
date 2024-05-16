package no.uio.ifi.in2000.team31

import no.uio.ifi.in2000.team31.ui.settings.celsiusToFahrenheit
import org.junit.Assert
import org.junit.Test

class FahrenheitTest {

    @Test
    fun testCelsiusToFahrenheitConvertionLow() {
        val celsius = 15
        val fahrenheit = celsiusToFahrenheit(celsius)
        Assert.assertEquals(59, fahrenheit)
    }
    @Test
    fun testCelsiusToFahrenheitConvertionHigh() {
        val celsius = 30
        val fahrenheit = celsiusToFahrenheit(celsius)
        Assert.assertEquals(86, fahrenheit)
    }
}