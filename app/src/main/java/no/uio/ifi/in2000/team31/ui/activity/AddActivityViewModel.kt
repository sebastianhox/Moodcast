package no.uio.ifi.in2000.team31.ui.activity

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import no.uio.ifi.in2000.team31.MoodApplication
import no.uio.ifi.in2000.team31.data.activity.Activity
import no.uio.ifi.in2000.team31.data.activity.ActivityRepository
import no.uio.ifi.in2000.team31.ui.mood.Mood

class AddActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val appContainer = (application as MoodApplication).appContainer
    private val activityRepository: ActivityRepository = appContainer.activityRepository
    var activityUiState by mutableStateOf(ActivityUiState())
        private set

    fun updateUiState(activityDetails: ActivityDetails) {
        activityUiState =
            ActivityUiState(activityDetails = activityDetails, isEntryValid = validateInput(activityDetails))
    }

    private fun validateInput(uiState: ActivityDetails = activityUiState.activityDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && info.isNotBlank()
        }
    }

    suspend fun saveActivity() {
        if (validateInput()) {
            activityRepository.insertActivity(activityUiState.activityDetails.toActivity())
        }
    }

}

data class ActivityUiState(
    val activityDetails: ActivityDetails = ActivityDetails(),
    val isEntryValid: Boolean = false
)

data class ActivityDetails(
    val id: Int = 0,
    val name: String = "",
    val info: String = "",
    val text: String = "",
    val imagePath: String? = null,
    val suitableMoods: List<Mood> = listOf()
)

fun ActivityDetails.toActivity(): Activity = Activity(
    id = id,
    name = name,
    info = info,
    imagePath = imagePath,
    suitableMoods = suitableMoods
)

fun Activity.toActivityUiState(isEntryValid: Boolean = false): ActivityUiState = ActivityUiState(
    activityDetails = this.toActivityDetails(),
    isEntryValid = isEntryValid
)

fun Activity.toActivityDetails(): ActivityDetails = ActivityDetails(
    id = id,
    name = name, // .toString()?
    info = info, // .toString()?
    imagePath = imagePath,
    suitableMoods = suitableMoods
)