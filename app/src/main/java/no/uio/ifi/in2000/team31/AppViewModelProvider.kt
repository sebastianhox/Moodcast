package no.uio.ifi.in2000.team31

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import no.uio.ifi.in2000.team31.ui.activity.ActivityScreenViewModel
import no.uio.ifi.in2000.team31.ui.activity.AddActivityViewModel
import no.uio.ifi.in2000.team31.ui.home.HomeViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            AddActivityViewModel(moodApplication().container.activityRepository)
        }
        initializer {
            ActivityScreenViewModel(moodApplication().container.activityRepository)
        }

    }
}

fun CreationExtras.moodApplication(): MoodApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MoodApplication)
