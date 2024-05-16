package no.uio.ifi.in2000.team31




import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import no.uio.ifi.in2000.team31.ui.mood.Mood
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
class TestAppContainer {
    private val mockConnectivityObserver = Mockito.mock(NetworkConnectivityObserver::class.java)

    fun getConnectivityObserver(): NetworkConnectivityObserver {
        return mockConnectivityObserver // Return the mocked observer
    }
}
@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {

    //@OptIn(ExperimentalCoroutinesApi::class)
    //@get:Rule
    //

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineDispatcher = MainDispatcherRule()

    @Mock
    private lateinit var testViewModel: SharedViewModel
    private lateinit var moodApplication: MoodApplication
    private lateinit var appContainer: AppContainer
    private lateinit var mockConnectivityObserver: NetworkConnectivityObserver

    private val mockMoodUIState = MutableStateFlow(MoodUIState())

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockitoAnnotations.openMocks(this)
        moodApplication = Mockito.mock(MoodApplication::class.java)
        appContainer = Mockito.mock(AppContainer::class.java)
        mockConnectivityObserver = Mockito.mock(NetworkConnectivityObserver::class.java)
        Mockito.`when`(moodApplication.appContainer).thenReturn(appContainer)
        Mockito.`when`(appContainer.connectivityObserver).thenReturn(mockConnectivityObserver)
        Mockito.`when`(testViewModel.moodUIState).thenReturn(mockMoodUIState)
        testViewModel = SharedViewModel(moodApplication)
    }
/*
    @Test
    fun testSomething() = runTest{
        testViewModel.setSelectedMood(Mood.STRESSED)
        advanceUntilIdle()
        Assert.assertEquals(Mood.STRESSED, testViewModel.moodUIState.value.selectedMood)
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