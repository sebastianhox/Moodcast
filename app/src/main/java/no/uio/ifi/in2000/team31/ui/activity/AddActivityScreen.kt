package no.uio.ifi.in2000.team31.ui.activity

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.R
import no.uio.ifi.in2000.team31.ui.mood.Mood
import no.uio.ifi.in2000.team31.ui.navigation.NavigationDestination
import java.io.File
import java.io.IOException

object AddActivityDestination : NavigationDestination {
    override val route = "add_activity"
    override val titleRes = R.string.add_activity
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(
    navigateBack: () -> Unit,
    viewModel: AddActivityViewModel = viewModel()
) {
//    var nameText by remember { mutableStateOf("") }
//    var infoText by remember { mutableStateOf("") }
//    var showError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val activityUiState = viewModel.activityUiState
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    "MoodCast",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 31.dp)
                )},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp)
                    .shadow(3.dp, RoundedCornerShape(bottomEnd = 4.dp, bottomStart = 4.dp)),
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            ActivityInputForm(
                activityDetails = activityUiState.activityDetails,
                onValueChange = viewModel::updateUiState,
                modifier = Modifier
                    .padding(20.dp)
            )
            SelectMoods(
                selectedMoods = activityUiState.activityDetails.suitableMoods,
                onMoodSelectionChange = { newSelectedMoods ->
                    viewModel.updateUiState(activityUiState.activityDetails.copy(suitableMoods = newSelectedMoods))
                },
                modifier = Modifier
                    .padding(20.dp)
            )
            SelectWeathers(
                selectedWeathers = activityUiState.activityDetails.suitableWeathers,
                onWeatherSelectionChange = { newSelectedWeathers ->
                    viewModel.updateUiState(activityUiState.activityDetails.copy(suitableWeathers = newSelectedWeathers))
                },
                modifier = Modifier
                    .padding(20.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.saveActivity()
                        navigateBack()
                    }
                },
                enabled = activityUiState.isEntryValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            ) {
                Text(
                    text = "Save",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                )
            }
        }

    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectWeathers(
    selectedWeathers: List<WeatherStatus>,
    onWeatherSelectionChange: (List<WeatherStatus>) -> Unit,
    modifier: Modifier
) {
    Column (
        modifier = modifier
    ) {
        Text("Passende værforhold:")

        Spacer(modifier = Modifier.size(10.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            WeatherStatus.entries.forEach { weather ->
                Log.d("chip", "loading: $weather")
                MoodChip(
                    label = {
                        Text(
                            text = weather.name,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    },
                    onClick = {
                        Log.d("chip", "chip clicked on")
                        val newSelectedWeathers = if (weather in selectedWeathers) {
                            selectedWeathers - weather
                        } else {
                            selectedWeathers + weather
                        }
                        onWeatherSelectionChange(newSelectedWeathers)
                    }
                )
            }

        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectMoods(
    selectedMoods: List<Mood>,
    onMoodSelectionChange: (List<Mood>) -> Unit,
    modifier: Modifier
) {
    Column (
        modifier = modifier
    ) {
        Text("Suitable moods:")

        Spacer(modifier = Modifier.size(10.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Mood.entries.forEach { mood ->
                Log.d("chip", "loading: $mood")
                MoodChip(
                    label = {
                        Text(
                            text = mood.name,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    },
                    onClick = {
                        Log.d("chip", "chip clicked on")
                        val newSelectedMoods = if (mood in selectedMoods) {
                            selectedMoods - mood
                        } else {
                            selectedMoods + mood
                        }
                        onMoodSelectionChange(newSelectedMoods)
                    }
                )
            }

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
    Column (
        modifier = modifier
    ) {
        OutlinedTextField(
            value = activityDetails.name,
            onValueChange = { onValueChange(activityDetails.copy(name = it)) },
            label = { Text("Activity name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = activityDetails.info,
            onValueChange = { onValueChange(activityDetails.copy(info = it)) },
            label = { Text("Activity info") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            enabled = enabled,
            singleLine = false
        )
        SelectPhotoFromGallery(
            activityDetails,
            onActivityValueChange = { newActivityDetails ->
                onValueChange(newActivityDetails)
            },
            modifier = Modifier.padding(5.dp)
        )
    }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SelectPhotoFromGallery(
    activityDetails: ActivityDetails,
    onActivityValueChange: (ActivityDetails) -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    var imageUrl by remember { mutableStateOf<Uri?>(null) }
    var imageFileName by remember { mutableStateOf<String?>(null) }
//    var showPermissionsRationale by remember { mutableStateOf(false) }
//    val storagePermission = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    val permissionToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    
    val permissionState = rememberPermissionState(permission = permissionToRequest)
    
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

    Column (
        modifier = modifier
    ) {
        if (permissionState.status.isGranted) {
            Button(onClick = {
                val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                pickMedia.launch(request)
            }) {
                Text("Pick an Image")
            }
            Text(text = imageFileName ?: "")   
        } else {
            Button(onClick = { 
                permissionState.launchPermissionRequest()
            }) {
                Text("Request permission")
            }
            Text("No access")
        }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodChip(
    label: @Composable () -> Unit,
    onClick: () -> Unit
) {
    var selected by remember { mutableStateOf(false) }

    FilterChip(
        onClick = {
            selected = !selected
            onClick()
                  },
        label = label,
        modifier = Modifier
            .padding(horizontal = 5.dp),
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }

    )
}

