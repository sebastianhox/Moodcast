package no.uio.ifi.in2000.team31.data.activity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import no.uio.ifi.in2000.team31.ui.activity.WeatherStatus
import no.uio.ifi.in2000.team31.ui.mood.Mood

class Converters {
    @TypeConverter
    fun fromMoodList(moods: List<Mood>): String {
        return Gson().toJson(moods)
    }

    @TypeConverter
    fun toMoodList(moodsString: String): List<Mood> {
        val listType = object : TypeToken<List<Mood>>() {}.type
        return Gson().fromJson(moodsString, listType)
    }

    @TypeConverter
    fun fromWeatherStatusList(weatherStatusList: List<WeatherStatus>): String {
        return Gson().toJson(weatherStatusList)
    }

    @TypeConverter
    fun toWeatherStatusList(weatherStatusString: String): List<WeatherStatus> {
        val listType = object : TypeToken<List<WeatherStatus>>() {}.type
        return Gson().fromJson(weatherStatusString, listType)
    }
}