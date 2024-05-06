package no.uio.ifi.in2000.team31

import no.uio.ifi.in2000.team31.data.geoname.GeonameDataSource
import no.uio.ifi.in2000.team31.data.geoname.GeonameRepository
import no.uio.ifi.in2000.team31.data.locationforecast.LocationWeatherDataSource
import no.uio.ifi.in2000.team31.data.locationforecast.LocationWeatherRepository
import no.uio.ifi.in2000.team31.data.weatheralert.WeatherAlertDataSource
import no.uio.ifi.in2000.team31.data.weatheralert.WeatherAlertRepository

class AppContainer {

    private val locWeatherDataSource = LocationWeatherDataSource()
    private val alertDataSource = WeatherAlertDataSource()
    private val geonameDataSource = GeonameDataSource()

    val locWeatherRepository = LocationWeatherRepository(locWeatherDataSource)
    val alertRepository = WeatherAlertRepository(alertDataSource)
    val geonameRepository = GeonameRepository(geonameDataSource)

}