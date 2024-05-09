package no.uio.ifi.in2000.team31

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import no.uio.ifi.in2000.team31.ui.mood.Mood

class SharedViewModel : ViewModel() {
    val selectedMood = MutableStateFlow<Mood?>(null)
}