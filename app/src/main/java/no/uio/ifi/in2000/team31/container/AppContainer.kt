package no.uio.ifi.in2000.team31.container

import android.app.Application
import android.content.Context
import no.uio.ifi.in2000.team31.data.network.NetworkConnectivityObserver
import no.uio.ifi.in2000.team31.ui.shared.SharedViewModel
import no.uio.ifi.in2000.team31.data.activity.ActivityDatabase
import no.uio.ifi.in2000.team31.data.activity.OfflineActivityRepository
import no.uio.ifi.in2000.team31.data.geoname.GeonameDataSource
import no.uio.ifi.in2000.team31.data.geoname.GeonameRepository
import no.uio.ifi.in2000.team31.data.locationforecast.LocationWeatherDataSource
import no.uio.ifi.in2000.team31.data.locationforecast.LocationWeatherRepository
import no.uio.ifi.in2000.team31.data.settings.SettingsRepository
import no.uio.ifi.in2000.team31.data.weatheralert.WeatherAlertDataSource
import no.uio.ifi.in2000.team31.data.weatheralert.WeatherAlertRepository
import no.uio.ifi.in2000.team31.ui.activity.ActivityViewModel
import no.uio.ifi.in2000.team31.ui.settings.SettingsViewModel

class AppContainer(private val context: Context) {

    private val locWeatherDataSource = LocationWeatherDataSource()
    private val alertDataSource = WeatherAlertDataSource()
    private val geonameDataSource = GeonameDataSource()


    val locWeatherRepository = LocationWeatherRepository(locWeatherDataSource)
    val alertRepository = WeatherAlertRepository(alertDataSource)
    val geonameRepository = GeonameRepository(geonameDataSource)
    val settingsRepository by lazy { SettingsRepository(context)}
    val activityRepository by lazy {
        OfflineActivityRepository(ActivityDatabase.detDatabase(context).activityDao())
    }

    val settingsViewModel by lazy { SettingsViewModel(context.applicationContext as Application) }
    val sharedViewModel by lazy { SharedViewModel(context.applicationContext as Application) }

    val connectivityObserver by lazy { NetworkConnectivityObserver(context.applicationContext) }
}
