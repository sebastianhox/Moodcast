package no.uio.ifi.in2000.team31.model

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("type"       ) var type       : String?     = null,
    @SerializedName("geometry"   ) var geometry   : Geometry?   = Geometry(),
    @SerializedName("properties" ) var properties : Properties? = Properties()
)



data class Geometry (

    @SerializedName("type"        ) var type        : String?        = null,
    @SerializedName("coordinates" ) var coordinates : ArrayList<Double> = arrayListOf()

)


data class Units (

    @SerializedName("air_pressure_at_sea_level" ) var airPressureAtSeaLevel : String? = null,
    @SerializedName("air_temperature"           ) var airTemperature        : String? = null,
    @SerializedName("cloud_area_fraction"       ) var cloudAreaFraction     : String? = null,
    @SerializedName("precipitation_amount"      ) var precipitationAmount   : String? = null,
    @SerializedName("relative_humidity"         ) var relativeHumidity      : String? = null,
    @SerializedName("wind_from_direction"       ) var windFromDirection     : String? = null,
    @SerializedName("wind_speed"                ) var windSpeed             : String? = null

)
data class Meta (

    @SerializedName("updated_at" ) var updatedAt : String? = null,
    @SerializedName("units"      ) var units     : Units?  = Units()

)


data class Details (

    @SerializedName("air_pressure_at_sea_level" ) var airPressureAtSeaLevel : Double?    = null,
    @SerializedName("air_temperature"           ) var airTemperature        : Double? = null,
    @SerializedName("cloud_area_fraction"       ) var cloudAreaFraction     : Double? = null,
    @SerializedName("relative_humidity"         ) var relativeHumidity      : Double? = null,
    @SerializedName("wind_from_direction"       ) var windFromDirection     : Double? = null,
    @SerializedName("wind_speed"                ) var windSpeed             : Double? = null,
    @SerializedName("precipitation_amount"      ) var precipitationAmount : Double? = null

)


data class Instant (

    @SerializedName("details" ) var details : Details? = Details()

)






data class Next12Hours (

    @SerializedName("summary" ) var summary : Summary? = Summary(),
    @SerializedName("details" ) var details : Details? = Details()

)

data class Summary (

    @SerializedName("symbol_code" ) var symbolCode : String? = null

)





data class Next1Hours (

    @SerializedName("summary" ) var summary : Summary? = Summary(),
    @SerializedName("details" ) var details : Details? = Details()

)



data class Next6Hours (

    @SerializedName("summary" ) var summary : Summary? = Summary(),
    @SerializedName("details" ) var details : Details? = Details()

)


data class Data (

    @SerializedName("instant"       ) var instant     : Instant?     = Instant(),
    @SerializedName("next_12_hours" ) var next12Hours : Next12Hours? = Next12Hours(),
    @SerializedName("next_1_hours"  ) var next1Hours  : Next1Hours?  = Next1Hours(),
    @SerializedName("next_6_hours"  ) var next6Hours  : Next6Hours?  = Next6Hours()

)


data class Timeseries (

    @SerializedName("time" ) var time : String? = null,
    @SerializedName("data" ) var data : Data?   = Data()

)


data class Properties (

    @SerializedName("meta"       ) var meta       : Meta?                 = Meta(),
    @SerializedName("timeseries" ) var timeseries : ArrayList<Timeseries> = arrayListOf()

)
fun WeatherData.toModelInstant(): WeatherDataModel {
    val instantList = mutableListOf<WeatherDataInstant>()
    properties?.timeseries?.forEach {
        instantList.add(
            WeatherDataInstant(
                time = it.time,
                airPressureAtSeaLevel = it.data?.instant?.details?.airPressureAtSeaLevel,
                airTemperature = it.data?.instant?.details?.airTemperature,
                cloudAreaFraction = it.data?.instant?.details?.cloudAreaFraction,
                relativeHumidity = it.data?.instant?.details?.relativeHumidity,
                windFromDirection = it.data?.instant?.details?.windFromDirection,
                windSpeed = it.data?.instant?.details?.windSpeed,
                symbolCode = it.data?.next1Hours?.summary?.symbolCode,
                precipitationAmount = it.data?.instant?.details?.precipitationAmount,
                next12Hours = it.data?.next12Hours?.details?.airTemperature
            )
        )
    }

    return WeatherDataModel(
        instant = instantList
    )
}