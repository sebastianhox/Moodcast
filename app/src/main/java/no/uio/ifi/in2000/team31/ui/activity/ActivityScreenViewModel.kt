package no.uio.ifi.in2000.team31.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.data.activity.Activity
import no.uio.ifi.in2000.team31.data.activity.ActivityRepository

class ActivityScreenViewModel(val activityRepository: ActivityRepository) : ViewModel() {
    val activityScreenUiState: StateFlow<ActivityScreenUiState> =
        activityRepository.getAllActivitiesStream().map { ActivityScreenUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ActivityScreenUiState()
            )
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }


    fun preloadActivity(activity: Activity) {
        viewModelScope.launch {
            activityRepository.insertActivity(activity)
        }
    }
}

data class ActivityScreenUiState(val activitiesList: List<Activity> = listOf())
