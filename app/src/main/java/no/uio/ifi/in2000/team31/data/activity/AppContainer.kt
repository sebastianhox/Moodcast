package no.uio.ifi.in2000.team31.data.activity

import android.content.Context

interface AppContainer {
    val activityRepository: ActivityRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val activityRepository: ActivityRepository by lazy {
        OfflineActivityRepository(ActivityDatabase.detDatabase(context).activityDao())
    }
}