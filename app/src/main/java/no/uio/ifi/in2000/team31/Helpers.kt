package no.uio.ifi.in2000.team31

import android.content.Context
import android.util.Log
import no.uio.ifi.in2000.team31.ui.activity.ActivityDetails
import no.uio.ifi.in2000.team31.ui.activity.ActivityScreenViewModel
import no.uio.ifi.in2000.team31.ui.activity.WeatherStatus
import no.uio.ifi.in2000.team31.ui.activity.toActivity
import no.uio.ifi.in2000.team31.ui.mood.Mood
import java.io.File
import java.io.IOException


fun getWindDirectionIcon(degrees: Int?): Int {
    return if (degrees != null) {
        when (degrees.toDouble()) {
            in 22.5..67.5 -> R.drawable.baseline_south_west_24   // 45
            in 67.5..112.5 -> R.drawable.baseline_west_24        // 90
            in 112.5..157.5 -> R.drawable.baseline_north_west_24 // 135
            in 157.5..202.5 -> R.drawable.baseline_north_24      // 180
            in 202.5..247.5 -> R.drawable.baseline_north_east_24 // 225
            in 247.5..292.5 -> R.drawable.baseline_east_24       // 270
            in 292.5..337.5 -> R.drawable.baseline_south_east_24 // 315
            else -> R.drawable.baseline_south_24                       // 0
        }
    } else {
        R.drawable.baseline_wind_power_24 // Bare som en default for at det skal se bra ut
    }
}

fun copyImageFromAssetsToStorage(context: Context, imageName: String): String? {
    Log.d("populus", "imageName: $imageName")
    val destinationFile = File(context.filesDir, imageName)

    try {
        context.assets.open(imageName).use { input ->
            destinationFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        Log.d("populus", "destination: $destinationFile")
        Log.d("populus", "abs path: ${destinationFile.absolutePath}")
        return destinationFile.absolutePath // Return the absolute path
    } catch (e: IOException) {
        Log.e("populus", "Error saving image: ${e.message}")
        return null
    }
}
fun populateDatabase(context: Context, viewModel: ActivityScreenViewModel) {
    var imagePath = copyImageFromAssetsToStorage(context, "running.jpg")
    if (imagePath != null) {
        val activityDetails = ActivityDetails(
            name = "Løpetur",
            info = "Løp en tur, godt for kropp og sinn!",
            imagePath = imagePath.toString(),
            suitableMoods = listOf(Mood.ENERGETIC, Mood.HAPPY),
            suitableWeathers = listOf(WeatherStatus.CLOUDY, WeatherStatus.SUNNY)
        )
        val activity = activityDetails.toActivity()
        viewModel.preloadActivity(activity)
    } else {
        Log.e("populus", "Failed to copy image for preloading")
    }
    imagePath = copyImageFromAssetsToStorage(context, "cycling.jpg")
    if (imagePath != null) {
        val activityDetails = ActivityDetails(
            name = "Sykling",
            info = "Ta en sykkeltur utendørs!",
            imagePath = imagePath.toString(),
            suitableMoods = listOf(Mood.HAPPY, Mood.ENERGETIC),
            suitableWeathers = listOf(WeatherStatus.CLOUDY, WeatherStatus.SUNNY)
        )
        val activity = activityDetails.toActivity()
        viewModel.preloadActivity(activity)
    } else {
        Log.e("populus", "Failed to copy image for preloading")
    }
}