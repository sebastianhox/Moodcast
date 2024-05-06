package no.uio.ifi.in2000.team31.model

import com.google.gson.annotations.SerializedName

data class GeonamesModel (
    val geonames: List<GeonameData> = listOf()
)
data class GeonameData (
    val placeName: String?,
    val country: String?,
    val adminName: String?,
    val lat: Double?,
    val lon: Double?
)

data class GeonameDTO (

    @SerializedName("totalResultsCount" ) var totalResultsCount : Int?                = null,
    @SerializedName("geonames"          ) var geonames          : ArrayList<Geonames> = arrayListOf()

)

data class AdminCodes1 (

    @SerializedName("ISO3166_2" ) var ISO31662 : String? = null

)

data class Geonames (

    @SerializedName("adminCode1"  ) var adminCode1  : String?      = null,
    @SerializedName("lng"         ) var lng         : Double?      = null,
    @SerializedName("geonameId"   ) var geonameId   : Int?         = null,
    @SerializedName("toponymName" ) var toponymName : String?      = null,
    @SerializedName("countryId"   ) var countryId   : String?      = null,
    @SerializedName("fcl"         ) var fcl         : String?      = null,
    @SerializedName("population"  ) var population  : Int?         = null,
    @SerializedName("countryCode" ) var countryCode : String?      = null,
    @SerializedName("name"        ) var name        : String?      = null,
    @SerializedName("fclName"     ) var fclName     : String?      = null,
    @SerializedName("adminCodes1" ) var adminCodes1 : AdminCodes1? = AdminCodes1(),
    @SerializedName("countryName" ) var countryName : String?      = null,
    @SerializedName("fcodeName"   ) var fcodeName   : String?      = null,
    @SerializedName("adminName1"  ) var adminName1  : String?      = null,
    @SerializedName("lat"         ) var lat         : Double?      = null,
    @SerializedName("fcode"       ) var fcode       : String?      = null

)

fun GeonameDTO.toModel(): GeonamesModel {
    val geonamesList = mutableListOf<GeonameData>()
    geonames.forEach {
        geonamesList.add(
            GeonameData(
            placeName = it.name,
            country = it.countryName,
            adminName = it.adminName1,
            lat = it.lat,
            lon = it.lng
            )
        )
    }
    return GeonamesModel(
        geonames = geonamesList
    )
}

fun GeonameDTO.toGeonameData(): GeonameData {
    return GeonameData(
        placeName = geonames[0].name,
        country = geonames[0].countryName,
        adminName = geonames[0].adminName1,
        lat = geonames[0].lat,
        lon = geonames[0].lng
    )
}