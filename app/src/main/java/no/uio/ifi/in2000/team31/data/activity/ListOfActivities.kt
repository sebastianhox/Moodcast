package no.uio.ifi.in2000.team31.data.activity

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import java.io.File
import java.io.IOException
/*
fun populateDatabase(context: Context) {
    copyImageFromAssetsToStorage(context, "running.jpg")


}

fun copyImageFromAssetsToStorage(context: Context, imageName: String): String? { // Image name in your Assets folder
    val filename = imageName // Optionally modify the filename if needed
    val destinationFile = File(context.filesDir, filename)

    try {
        context.assets.open("images/$imageName").use { input -> // Assumes "images" folder in assets
            destinationFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return destinationFile.absolutePath
    } catch (e: IOException) {
        Log.e("copy-from-assets", "Error saving image: ${e.message}")
        return null
    }
}
*/