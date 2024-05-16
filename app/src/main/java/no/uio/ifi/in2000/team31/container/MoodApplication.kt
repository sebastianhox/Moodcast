package no.uio.ifi.in2000.team31.container

import android.app.Application
import no.uio.ifi.in2000.team31.container.AppContainer

class MoodApplication: Application() {
    val appContainer = AppContainer(this)

}