package no.uio.ifi.in2000.team31

import android.content.Context
import no.uio.ifi.in2000.team31.data.activity.ActivityDatabase
import no.uio.ifi.in2000.team31.data.activity.ActivityRepository
import no.uio.ifi.in2000.team31.data.activity.OfflineActivityRepository
import no.uio.ifi.in2000.team31.data.geoname.GeonameDataSource
import no.uio.ifi.in2000.team31.data.geoname.GeonameRepository
import no.uio.ifi.in2000.team31.data.locationforecast.LocationWeatherDataSource
import no.uio.ifi.in2000.team31.data.locationforecast.LocationWeatherRepository
import no.uio.ifi.in2000.team31.data.weatheralert.WeatherAlertDataSource
import no.uio.ifi.in2000.team31.data.weatheralert.WeatherAlertRepository

class AppContainer(private val context: Context) {
    val activityRepository: ActivityRepository by lazy {
        OfflineActivityRepository(ActivityDatabase.detDatabase(context).activityDao())
    }

    private val locWeatherDataSource = LocationWeatherDataSource()
    private val alertDataSource = WeatherAlertDataSource()
    private val geonameDataSource = GeonameDataSource()


    val locWeatherRepository = LocationWeatherRepository(locWeatherDataSource)
    val alertRepository = WeatherAlertRepository(alertDataSource)
    val geonameRepository = GeonameRepository(geonameDataSource)
    val sharedViewModel = SharedViewModel()
}
