package no.uio.ifi.in2000.team31




import android.app.Application
import android.app.Instrumentation
import android.content.Context
import no.uio.ifi.in2000.team31.SharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import no.uio.ifi.in2000.team31.ui.activity.WeatherStatus
import no.uio.ifi.in2000.team31.ui.mood.Mood
import no.uio.ifi.in2000.team31.ui.settings.celsiusToFahrenheit
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.manipulation.Ordering

@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    var mainCoroutineDispatcher = MainDispatcherRule()



    /*@Test
    fun testSetLocation() = runTest {
        testSharedViewModel.updateLocation(12.4, 8.4)
        advanceUntilIdle()
        assertEquals(12.4, testSharedViewModel.locationUIState.value.lat)
        assertEquals(8.4, testSharedViewModel.locationUIState.value.lon)
    }*/



}

@ExperimentalCoroutinesApi
class MainDispatcherRule @JvmOverloads constructor(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}