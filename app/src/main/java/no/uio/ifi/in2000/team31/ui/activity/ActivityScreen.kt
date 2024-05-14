package no.uio.ifi.in2000.team31.ui.activity

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.MoodApplication
import no.uio.ifi.in2000.team31.data.activity.Activity
import no.uio.ifi.in2000.team31.data.activity.ActivityRepository
import no.uio.ifi.in2000.team31.ui.mood.Mood
import no.uio.ifi.in2000.team31.ui.navigation.BottomNavigationBar
import java.io.File
import java.io.IOException

enum class WeatherStatus {
    SUNNY,
    RAINY,
    SNOWY,
    CLOUDY
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
@Composable
fun ActivityScreen(
    navigateToAddActivity: () -> Unit,
    navController: NavController,
    viewModel: ActivityScreenViewModel = viewModel()
) {

    val appContainer = (LocalContext.current.applicationContext as MoodApplication).appContainer
    val sharedViewModel = appContainer.sharedViewModel

    val userMood by sharedViewModel.moodUIState.collectAsState()
    val currentWeatherStatus by sharedViewModel.weatherUIState.collectAsState()

    val activityScreenUiState by viewModel.activityScreenUiState.collectAsState()
//    var scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Log.d("moodfix", "UserMood is $userMood")
    Scaffold(
        topBar = {
            MoodCastTopBar()
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },

        floatingActionButton = {
            FloatingActionButton(onClick = navigateToAddActivity) {
                Icon(Icons.Filled.Add, "")
            }
        }
    ) { innerPadding ->
        ActivityScreenBody(
            activityList = activityScreenUiState.activitiesList,
            contentPadding = innerPadding,
            viewModel = viewModel,
            userMood = userMood.selectedMood,
            currentWeather = currentWeatherStatus.currentWeatherStatus,
            navController = navController
        )
    }
}

@Composable
fun ActivityScreenBody(
    activityList: List<Activity>,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: ActivityScreenViewModel,
    userMood: Mood?,
    currentWeather: WeatherStatus?,
    navController: NavController
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.activityRepository.getAllActivitiesStream().firstOrNull()?.let { existingActivity ->
            if (existingActivity.isEmpty()) {
                Log.d("populus", "IS EMPTY")
                populateDatabase(context, viewModel)
            } else {
                Log.d("populus", "NOT EMPTY")
            }
        }
    }
    Column(
        modifier = Modifier.padding(contentPadding)
    ) {
        if (userMood != null) {
            //Text("Aktiviteter for humør: ${userMood.name}")
            Log.d("moodScreen", "Weatherstatus ${userMood.name}")
        } else {
            //Text("Alle aktiviteter")
            Log.d("moodScreen", "No mood found")
        }
        if (currentWeather != null) {
            //Text("Værstatus: $currentWeather")
            Log.d("moodScreen", "Weatherstatus $currentWeather")
        } else {
            Text("Værstatus ikke funnet. Si ifra så fikser vi denne bugen?")
            Log.d("moodScreen", "No weatherstatus found")
        }

        HeroText(status = currentWeather, modifier = Modifier.padding(12.dp) )

        val filteredList = activityList.filter { activity ->
            val matchesMood = userMood == null || activity.suitableMoods.contains(userMood)
            val matchesWeather = currentWeather == null || activity.suitableWeathers.contains(currentWeather)
            matchesMood && matchesWeather
        }

        ActivityCardList(
            activityList = filteredList,
            viewModel = viewModel,
            onActivityClick = { activity ->
                navController.navigate("activityDetails/${activity.id}")
            }
        )
    }
}

@Composable
fun HeroText(status: WeatherStatus?, modifier: Modifier) {
    val text = when (status) {
        WeatherStatus.SUNNY -> "Solfylt og glad?"
        WeatherStatus.RAINY -> "Regn, regn, regn!"
        WeatherStatus.SNOWY -> "Snø, snø, snø!"
        WeatherStatus.CLOUDY -> "Overskyet..."
        null -> "Mangler værstatus"
    }
    val description = when (status) {
        WeatherStatus.SUNNY -> "Ta en tur ut i det fine været, nyt en piknik i parken eller utforsk byen på sykkel for en perfekt dag!"
        WeatherStatus.RAINY -> "Det er en flott dag for innendørsaktiviteter. Hva med å besøke et museum eller se en god film?"
        WeatherStatus.SNOWY -> "Kle deg varmt og nyt vinterlandskapet. Hva med å bygge en snømann?"
        WeatherStatus.CLOUDY -> "En perfekt dag for litt rolig tid. Kanskje lese en bok eller nyte en kaffe på en koselig kafé?"
        null -> "Noe har gått galt..."
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(Modifier.padding(horizontal = 14.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = description,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodCastTopBar() {
    TopAppBar(
        title = { Text(
            "MoodCast",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp)
            .shadow(3.dp, RoundedCornerShape(bottomEnd = 4.dp, bottomStart = 4.dp))
    )
}

@Composable
fun ActivityCardList (
    activityList: List<Activity>,
    viewModel: ActivityScreenViewModel,
    onActivityClick: (Activity) -> Unit
) {
    val scope = rememberCoroutineScope()
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text(
            "Anbefalte aktivtiteter",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp))
        Spacer(modifier = Modifier.height(15.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(activityList) { activity ->
                ActivityCard(
                    activity,
                    onDeleteClick = {activityToDelete ->
                        scope.launch {
                            viewModel.activityRepository.deleteActivity(activityToDelete)
                        }
                    },
                    onClick = onActivityClick
                )
            }
        }
    }
}

@Composable
fun ActivityCard(
    activity: Activity,
    onDeleteClick: (Activity) -> Unit,
    onClick: (Activity) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier.clickable { onClick(activity) }
    ) {
        Box {
            Column(modifier = Modifier.padding(8.dp)) {
                AsyncImage(
                    model = activity.imagePath,
                    contentDescription = activity.name,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(activity.name, style = MaterialTheme.typography.titleMedium)
                Text(activity.info, style = MaterialTheme.typography.titleMedium)
            }
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Delete")
            }
        }

    }
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete?") },
            text = { Text("Sure?") },
            confirmButton = {
                            Button(onClick = {
                                onDeleteClick(activity)
                                showDeleteDialog = false
                            }) {
                                Text("Delete")
                            }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailsScreen(activityId: Int, activityRepository: ActivityRepository, onBackClick: () -> Unit) {

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
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val activityFlow: Flow<Activity?> = activityRepository.getItemStream(activityId)
        val activity by activityFlow.collectAsState(initial = null)

        if (activity == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Display activity details
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = activity!!.imagePath,
                    contentDescription = "Activity Image",
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(text = "Activity Name: ${activity!!.name}")
                Text(text = "Activity Name: ${activity!!.info}")
            }
        }

    }
}
