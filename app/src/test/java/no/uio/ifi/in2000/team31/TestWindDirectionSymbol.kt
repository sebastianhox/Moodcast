package no.uio.ifi.in2000.team31

import no.uio.ifi.in2000.team31.model.getWindDirectionIcon
import org.junit.Assert.assertEquals
import org.junit.Test
class TestWindDirectionSymbol {

    @Test
    fun getWindDirectionIcon_returnsCorrectIcon_forEachCardinalDirection() {
        assertEquals(R.drawable.baseline_south_24, getWindDirectionIcon(0))
        assertEquals(R.drawable.baseline_south_24, getWindDirectionIcon(360)) // Ensure wrapping

        assertEquals(R.drawable.baseline_west_24, getWindDirectionIcon(90))
        assertEquals(R.drawable.baseline_north_24, getWindDirectionIcon(180))
        assertEquals(R.drawable.baseline_east_24, getWindDirectionIcon(270))
    }

    @Test
    fun getWindDirectionIcon_returnsCorrectIcon_forEachIntercardinalDirection() {
        assertEquals(R.drawable.baseline_south_west_24, getWindDirectionIcon(45))
        assertEquals(R.drawable.baseline_north_west_24, getWindDirectionIcon(135))
        assertEquals(R.drawable.baseline_north_east_24, getWindDirectionIcon(225))
        assertEquals(R.drawable.baseline_south_east_24, getWindDirectionIcon(315))
    }

    @Test
    fun getWindDirectionIcon_returnsCorrectIcon_forBorderlineValues() {
        assertEquals(R.drawable.baseline_south_24, getWindDirectionIcon(22))
        assertEquals(R.drawable.baseline_south_west_24, getWindDirectionIcon(67))

    }

    @Test
    fun getWindDirectionIcon_returnsDefaultIcon_forNullInput() {
        assertEquals(R.drawable.baseline_wind_power_24, getWindDirectionIcon(null))
    }

}