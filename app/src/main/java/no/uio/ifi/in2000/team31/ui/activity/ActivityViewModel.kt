package no.uio.ifi.in2000.team31.ui.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.container.MoodApplication
import no.uio.ifi.in2000.team31.data.activity.Activity
import no.uio.ifi.in2000.team31.data.activity.ActivityRepository

data class ActivityScreenUiState(
    val activity: Activity? = null,
    val activitiesList: List<Activity> = listOf()

)

class ActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val appContainer = (application as MoodApplication).appContainer
    val activityRepository: ActivityRepository = appContainer.activityRepository
    private val _activitiesList = activityRepository.getAllActivitiesStream()
    private val _activity  = MutableStateFlow<Activity?>(null)
    val activityScreenUiState = combine(
        _activity,
        _activitiesList
    ) { activity, activitiesList ->

        ActivityScreenUiState(
            activity,
            activitiesList

        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = ActivityScreenUiState()
    )
    fun deleteActivity(activity: Activity) {
        viewModelScope.launch {
            activityRepository.deleteActivity(activity)
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


    fun preloadActivity(activity: Activity) {
        viewModelScope.launch {
            activityRepository.insertActivity(activity)
        }
    }
}


