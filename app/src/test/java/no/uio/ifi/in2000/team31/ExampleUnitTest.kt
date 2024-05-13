package no.uio.ifi.in2000.team31

import no.uio.ifi.in2000.team31.ui.settings.celsiusToFahrenheit
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testCelsiusToFahrenheitConvertion() {
        val celsius: Int = 15
        val fahrenheit = celsiusToFahrenheit(celsius)
        assertEquals(59, fahrenheit)
    }
}