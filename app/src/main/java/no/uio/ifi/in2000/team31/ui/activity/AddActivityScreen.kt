package no.uio.ifi.in2000.team31.ui.activity

import android.Manifest
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.AppViewModelProvider
import no.uio.ifi.in2000.team31.R
import no.uio.ifi.in2000.team31.data.activity.Activity
import no.uio.ifi.in2000.team31.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.team31.ui.settings.SettingsViewModel
import java.io.File
import java.io.IOException

object AddActivityDestination : NavigationDestination {
    override val route = "add_activity"
    override val titleRes = R.string.add_activity
}
@Composable
fun AddActivityScreen(
    navigateBack: () -> Unit,
    navController: NavController,
    settingsViewModel: SettingsViewModel,
    viewModel: AddActivityViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var nameText by remember { mutableStateOf("") }
    var infoText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Scaffold { innerPadding ->
        AddActivityBody(
            activityUiState = viewModel.activityUiState,
            onActivityValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveActivity()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }

}

@Composable
fun AddActivityBody(
    activityUiState: ActivityUiState,
    onActivityValueChange: (ActivityDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier
) {
    Column {
        ActivityInputForm(
            activityDetails = activityUiState.activityDetails,
            onValueChange = onActivityValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = activityUiState.isEntryValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

@Composable
fun ActivityInputForm(
    activityDetails: ActivityDetails,
    onValueChange: (ActivityDetails) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier
) {
    Column {

        OutlinedTextField(
            value = activityDetails.name,
            onValueChange = { onValueChange(activityDetails.copy(name = it)) },
            label = { Text("Activity name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = activityDetails.info,
            onValueChange = { onValueChange(activityDetails.copy(info = it)) },
            label = { Text("Activity info") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = false
        )
        SelectPhotoFromGallery(
            activityDetails,
            onActivityValueChange = { newActivityDetails ->
                onValueChange(newActivityDetails)
            }
        )
        if (enabled) {
            Text("Required fields")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SelectPhotoFromGallery(
    activityDetails: ActivityDetails,
    onActivityValueChange: (ActivityDetails) -> Unit
) {
    val context = LocalContext.current
    var imageUrl by remember { mutableStateOf<Uri?>(null) }
    var imageFileName by remember { mutableStateOf("No image selected") }

    var showPermissionsRationale by remember { mutableStateOf(false) }

    val storagePermission = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { imageUri ->
            val newFilePath = copyImageToStorage(context, imageUri)
            newFilePath?.let { path ->
                imageFileName = getFilenameFromUri(context, imageUri)
                imageUrl = null
                onActivityValueChange(activityDetails.copy(imagePath = path))
            }
        }
    }


    Column {
        Button(onClick = {
            val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            pickMedia.launch(request)
        }) {
            Text("Pick an Image")
        }
        Text(text = "Image: $imageFileName")
    }
}
fun getFilenameFromUri(context: Context, uri: Uri): String {
    return context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        cursor.getString(nameIndex)
    } ?: "Unknown"
}

private fun copyImageToStorage(context: Context, imageUri: Uri): String? {
    val filename = getFilenameFromUri(context, imageUri)
    val destinationFile = File(context.filesDir, filename)

    try {
        context.contentResolver.openInputStream(imageUri)?.use { input ->
            destinationFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return destinationFile.absolutePath
    } catch (e: IOException) {
        Log.e("get-image", "Error saving image: ${e.message}")
        return null
    }
}
