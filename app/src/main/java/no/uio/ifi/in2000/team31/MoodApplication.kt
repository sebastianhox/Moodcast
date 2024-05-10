package no.uio.ifi.in2000.team31

import android.app.Application

class MoodApplication : Application() {
    lateinit var container: AppDataContainer
    val sharedViewModel = SharedViewModel()

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}