package no.uio.ifi.in2000.team31.ui.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import no.uio.ifi.in2000.team31.data.activity.ActivityRepository

class ActivityViewModel(private val activityRepository: ActivityRepository) : ViewModel() {
    var activityUiState by mutableStateOf(ActivityUiState())
}

data class ActivityUiState(
    val activityDetails: ActivityDetails = ActivityDetails()
)

data class ActivityDetails(
    val id: Int = 0,
    val name: String = "",
    val info: String = ""
)