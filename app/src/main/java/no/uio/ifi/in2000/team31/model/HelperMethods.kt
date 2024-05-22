package no.uio.ifi.in2000.team31.model

import android.content.Context
import android.util.Log
import no.uio.ifi.in2000.team31.R
import no.uio.ifi.in2000.team31.ui.activity.ActivityDetails
import no.uio.ifi.in2000.team31.ui.activity.ActivityViewModel
import no.uio.ifi.in2000.team31.ui.activity.WeatherStatus
import no.uio.ifi.in2000.team31.ui.activity.toActivity
import no.uio.ifi.in2000.team31.ui.mood.Mood
import java.io.File
import java.io.IOException


fun getWeatherStatus(symbolCode: String?): WeatherStatus {
    return when {
        symbolCode?.startsWith("clearsky") == true -> WeatherStatus.SOL
        symbolCode?.startsWith("fair") == true -> WeatherStatus.SOL
        symbolCode?.startsWith("partlycloudy") == true -> WeatherStatus.SKYER
        symbolCode?.startsWith("cloudy") == true -> WeatherStatus.SKYER
        symbolCode?.contains("rain") == true -> WeatherStatus.REGN
        symbolCode?.contains("sleet") == true -> WeatherStatus.REGN
        else -> WeatherStatus.SKYER // Default to cloudy if not matched
    }
}

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
fun populateDatabase(context: Context, viewModel: ActivityViewModel) {
    var imagePath = copyImageFromAssetsToStorage(context, "running.jpg")
    if (imagePath != null) {
        val activityDetails = ActivityDetails(
            name = "Løpetur",
            info = "Løp en tur, godt for kropp og sinn!",
            imagePath = imagePath.toString(),
            suitableMoods = listOf(Mood.ENERGISK, Mood.GLAD),
            suitableWeathers = listOf(WeatherStatus.SKYER, WeatherStatus.SOL)
        )
        val activity = activityDetails.toActivity()
        viewModel.preloadActivity(activity)
    } else {
        Log.e("ImageLoading", "Failed to copy image for preloading")
    }
    imagePath = copyImageFromAssetsToStorage(context, "cycling.jpg")
    if (imagePath != null) {
        val activityDetails = ActivityDetails(
            name = "Sykling",
            info = "Ta en sykkeltur utendørs!",
            imagePath = imagePath.toString(),
            suitableMoods = listOf(Mood.GLAD, Mood.ENERGISK),
            suitableWeathers = listOf(WeatherStatus.SKYER, WeatherStatus.SOL)
        )
        val activity = activityDetails.toActivity()
        viewModel.preloadActivity(activity)
    } else {
        Log.e("ImageLoading", "Failed to copy image for preloading")
    }

    addActivity(
        context = context,
        viewModel = viewModel,
        name = "Se solnedgange på stranda",
        info = "Alene, eller med noen du er glad i",
        imageName = "beach.jpg",
        suitableMoods = listOf(Mood.GLAD, Mood.ENERGISK, Mood.ROLIG, Mood.TRIST, Mood.STRESSET),
        suitableWeathers = listOf(WeatherStatus.SOL, WeatherStatus.SKYER)
    )
    addActivity(
        context = context,
        viewModel = viewModel,
        name = "Brettspill",
        info = "Perfekt innendørs aktivitet",
        imageName = "boardgame.jpg",
        suitableMoods = listOf(Mood.GLAD, Mood.ENERGISK, Mood.ROLIG, Mood.TRIST, Mood.STRESSET),
        suitableWeathers = listOf(WeatherStatus.SOL, WeatherStatus.SKYER)
    )
    addActivity(
        context = context,
        viewModel = viewModel,
        name = "Utforsk nærområdet",
        info = "Eller et annet sted. Det er alltid nye områder å utforske. Gjerne ta med venner, eller alene om det er det du foretrekker.",
        imageName = "explore.jpg",
        suitableMoods = listOf(Mood.GLAD, Mood.ENERGISK, Mood.ROLIG, Mood.TRIST, Mood.STRESSET),
        suitableWeathers = listOf(WeatherStatus.SOL, WeatherStatus.SKYER)
    )
    addActivity(
        context = context,
        viewModel = viewModel,
        name = "Les en god bok",
        info = "Dette kan du gjøre både inne og ute, avhengig av vær. Hva med å lese en gammel favoritt, en ulest klassiker eller noe helt annet?",
        imageName = "read.jpg",
        suitableMoods = listOf(Mood.GLAD, Mood.ENERGISK, Mood.ROLIG, Mood.TRIST, Mood.STRESSET, Mood.SINT),
        suitableWeathers = listOf(WeatherStatus.SOL, WeatherStatus.SKYER, WeatherStatus.SNO, WeatherStatus.REGN)
    )
    addActivity(
        context = context,
        viewModel = viewModel,
        name = "Skolearbeid",
        info = "Enten det er til skolen eller universitetet, eller om det er noe du driver med på fritiden?",
        imageName = "study.jpg",
        suitableMoods = listOf(Mood.GLAD, Mood.ENERGISK, Mood.ROLIG, Mood.TRIST, Mood.STRESSET),
        suitableWeathers = listOf(WeatherStatus.SOL, WeatherStatus.SKYER)
    )
    addActivity(
        context = context,
        viewModel = viewModel,
        name = "Besøk familien",
        info = "Det er fint å tilbringe tid med de en er glad i.",
        imageName = "family.jpg",
        suitableMoods = listOf(Mood.GLAD, Mood.ENERGISK, Mood.ROLIG, Mood.TRIST, Mood.STRESSET),
        suitableWeathers = listOf(WeatherStatus.SOL, WeatherStatus.SKYER)
    )
    addActivity(
        context = context,
        viewModel = viewModel,
        name = "Shopping",
        info = "Gå innom en lokal butikk, eller innom kjøpesenteret",
        imageName = "shopping.jpg",
        suitableMoods = listOf(Mood.GLAD, Mood.ENERGISK, Mood.ROLIG, Mood.TRIST, Mood.STRESSET, Mood.SINT),
        suitableWeathers = listOf(WeatherStatus.SOL, WeatherStatus.SKYER)
    )
    addActivity(
        context = context,
        viewModel = viewModel,
        name = "Ta en kopp te",
        info = "Alene, eller med noen du er glad i",
        imageName = "tea.jpg",
        suitableMoods = listOf(Mood.GLAD, Mood.ENERGISK, Mood.ROLIG, Mood.TRIST, Mood.STRESSET),
        suitableWeathers = listOf(WeatherStatus.SNO, WeatherStatus.REGN, WeatherStatus.SKYER)
    )
    addActivity(
        context = context,
        viewModel = viewModel,
        name = "Spill fotball",
        info = "Både gøy, sosialt og bra for helsa!",
        imageName = "tea.jpg",
        suitableMoods = listOf(Mood.GLAD, Mood.ENERGISK, Mood.STRESSET, Mood.SINT),
        suitableWeathers = listOf(WeatherStatus.SOL, WeatherStatus.SKYER)
    )
    addActivity(
        context = context,
        viewModel = viewModel,
        name = "Møt noen venner",
        info = "Gå ut og gjør noe? Se en film sammen?",
        imageName = "friends.jpg",
        suitableMoods = listOf(Mood.GLAD, Mood.ENERGISK, Mood.ROLIG, Mood.TRIST, Mood.STRESSET),
        suitableWeathers = listOf(WeatherStatus.SOL, WeatherStatus.SKYER)
    )
}

fun addActivity(context: Context, viewModel: ActivityViewModel, name: String, info: String, imageName: String, suitableMoods: List<Mood>, suitableWeathers: List<WeatherStatus>) {
    val imageString = copyImageFromAssetsToStorage(context, imageName)
    val activityDetails = ActivityDetails(
        name = name,
        info = info,
        imagePath = imageString,
        suitableMoods = suitableMoods,
        suitableWeathers = suitableWeathers
    )
    viewModel.preloadActivity(activityDetails.toActivity())
}