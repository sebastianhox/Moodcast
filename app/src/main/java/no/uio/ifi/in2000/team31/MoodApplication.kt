package no.uio.ifi.in2000.team31

import android.app.Application
import android.content.Context

class MoodApplication() : Application() {
    val appContainer = AppContainer(this)

}