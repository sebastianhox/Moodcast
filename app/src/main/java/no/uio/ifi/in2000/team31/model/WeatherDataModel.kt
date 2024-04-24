package no.uio.ifi.in2000.team31.model
data class WeatherDataModel (
    val instant: List<WeatherDataInstant> = listOf() //ANTAR AT DET ER STUFF - NÅ
)

/*
Objekter av TimeSeries (bytta navn ig)
Hver TimesSeries objekt i den listen har 1 time "mellomrom" fra hverandre (APIet er sånn)
 */


data class WeatherDataInstant (
    val time                  : String? = null,
    val airPressureAtSeaLevel : Double? = null,
    val airTemperature        : Double? = null,
    val cloudAreaFraction     : Double? = null,
    val relativeHumidity      : Double? = null,
    val windFromDirection     : Double? = null,
    val windSpeed             : Double? = null,
    val symbolCode            : String? = null,
    val precipitationAmount   : Double? = null,
    val next12Hours           : Double? = null
)