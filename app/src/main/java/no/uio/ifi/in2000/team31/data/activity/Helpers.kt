package no.uio.ifi.in2000.team31.data.activity

import android.content.Context
import android.util.Log
import no.uio.ifi.in2000.team31.ui.activity.ActivityDetails
import no.uio.ifi.in2000.team31.ui.activity.ActivityScreenViewModel
import no.uio.ifi.in2000.team31.ui.activity.WeatherStatus
import no.uio.ifi.in2000.team31.ui.activity.toActivity
import no.uio.ifi.in2000.team31.ui.mood.Mood
import java.io.File
import java.io.IOException


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

fun addActivityToDb(
    context: Context,
    viewModel: ActivityScreenViewModel,
    name: String,
    info: String,
    imagePath: String,
    suitableMoods: List<Mood>,
    suitableWeathers: List<WeatherStatus>
) {
    val imageString = copyImageFromAssetsToStorage(context, imagePath)
    if (imageString != null) {
        val activityDetails = ActivityDetails(
            name = name,
            info = info,
            imagePath = imageString,
            suitableMoods = suitableMoods,
            suitableWeathers = suitableWeathers
        )
        val activity = activityDetails.toActivity()
        viewModel.preloadActivity(activity)
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

    addActivityToDb(
        context,
        viewModel,
        "Lese en bok",
        "Hvor som helst, når som helst",
        "reading.jpeg",
        suitableMoods = listOf(Mood.HAPPY, Mood.SAD, Mood.CALM, Mood.STRESSED),
        suitableWeathers = listOf(WeatherStatus.SUNNY, WeatherStatus.CLOUDY, WeatherStatus.RAINY, WeatherStatus.SNOWY)
    )

    addActivityToDb(
        context,
        viewModel,
        "Drikk en kopp te",
        "Det er godt vettu",
        "drinking-tea.jpg",
        suitableMoods = listOf(Mood.HAPPY, Mood.SAD, Mood.CALM, Mood.STRESSED, Mood.ENERGETIC, Mood.ANGRY),
        suitableWeathers = listOf(WeatherStatus.SUNNY, WeatherStatus.CLOUDY, WeatherStatus.RAINY, WeatherStatus.SNOWY)
    )
}