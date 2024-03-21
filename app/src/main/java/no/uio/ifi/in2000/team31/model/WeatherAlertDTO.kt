package no.uio.ifi.in2000.team31.model

import com.google.gson.annotations.SerializedName

data class WeatherAlert (
    @SerializedName("features"   ) var features   : ArrayList<Features> = arrayListOf(),
    @SerializedName("lang"       ) var lang       : String?             = null,
    @SerializedName("lastChange" ) var lastChange : String?             = null,
    @SerializedName("type"       ) var type       : String?             = null
)
data class AlertGeometry (
    @SerializedName("coordinates" ) var coordinates      : ArrayList<ArrayList<ArrayList<Double>>>            = arrayListOf(),
    @SerializedName("coordinates" ) var multiCoordinates : ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> = arrayListOf(),
    @SerializedName("type"        ) var type             : String?                                            = null
)
data class Resources (
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("mimeType"    ) var mimeType    : String? = null,
    @SerializedName("uri"         ) var uri         : String? = null
)
data class AlertProperties (
    @SerializedName("altitude_above_sea_level" ) var altitudeAboveSeaLevel : Int?                 = null,
    @SerializedName("area"                     ) var area                  : String?              = null,
    @SerializedName("awarenessResponse"        ) var awarenessResponse     : String?              = null,
    @SerializedName("awarenessSeriousness"     ) var awarenessSeriousness  : String?              = null,
    @SerializedName("awareness_level"          ) var awarenessLevel        : String?              = null,
    @SerializedName("awareness_type"           ) var awarenessType         : String?              = null,
    @SerializedName("ceiling_above_sea_level"  ) var ceilingAboveSeaLevel  : Int?                 = null,
    @SerializedName("certainty"                ) var certainty             : String?              = null,
    @SerializedName("consequences"             ) var consequences          : String?              = null,
    @SerializedName("contact"                  ) var contact               : String?              = null,
    @SerializedName("county"                   ) var county                : ArrayList<String>    = arrayListOf(),
    @SerializedName("description"              ) var description           : String?              = null,
    @SerializedName("event"                    ) var event                 : String?              = null,
    @SerializedName("eventAwarenessName"       ) var eventAwarenessName    : String?              = null,
    @SerializedName("eventEndingTime"          ) var eventEndingTime       : String?              = null,
    @SerializedName("geographicDomain"         ) var geographicDomain      : String?              = null,
    @SerializedName("id"                       ) var id                    : String?              = null,
    @SerializedName("instruction"              ) var instruction           : String?              = null,
    @SerializedName("resources"                ) var resources             : ArrayList<Resources> = arrayListOf(),
    @SerializedName("riskMatrixColor"          ) var riskMatrixColor       : String?              = null,
    @SerializedName("severity"                 ) var severity              : String?              = null,
    @SerializedName("status"                   ) var status                : String?              = null,
    @SerializedName("title"                    ) var title                 : String?              = null,
    @SerializedName("triggerLevel"             ) var triggerLevel          : String?              = null,
    @SerializedName("type"                     ) var type                  : String?              = null,
    @SerializedName("web"                      ) var web                   : String?              = null
)
data class When (
    @SerializedName("interval" ) var interval : ArrayList<String> = arrayListOf()
)
data class Features (
    @SerializedName("geometry"   ) var geometry   : AlertGeometry?   = AlertGeometry(),
    @SerializedName("properties" ) var properties : AlertProperties? = AlertProperties(),
    @SerializedName("type"       ) var type       : String?     = null,
    @SerializedName("when"       ) var whenIs     : When?       = When()
)