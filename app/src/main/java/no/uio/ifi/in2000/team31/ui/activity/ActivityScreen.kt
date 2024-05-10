package no.uio.ifi.in2000.team31.ui.activity

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.flow.firstOrNull
import no.uio.ifi.in2000.team31.AppViewModelProvider
import no.uio.ifi.in2000.team31.MoodApplication
import no.uio.ifi.in2000.team31.SharedViewModel
import no.uio.ifi.in2000.team31.data.activity.Activity
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
    val filename = imageName
    val destinationFile = File(context.filesDir, filename)

    try {
        context.assets.open(imageName).use { input ->
            destinationFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        Log.d("populus", "destination: ${destinationFile.toString()}")
        Log.d("populus", "abs path: ${destinationFile.absolutePath.toString()}")
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
            name = "Running",
            info = "Go for a run!",
            imagePath = imagePath.toString(),
            suitableMoods = listOf(Mood.ENERGETIC, Mood.HAPPY, Mood.ANGRY)
        )
        val activity = activityDetails.toActivity()
        viewModel.preloadActivity(activity)
    } else {
        Log.e("populus", "Failed to copy image for preloading")
    }
    imagePath = copyImageFromAssetsToStorage(context, "cycling.jpg")
    if (imagePath != null) {
        val activityDetails = ActivityDetails(
            name = "Cycling",
            info = "Enjoy a trip on your bike!",
            imagePath = imagePath.toString(),
            suitableMoods = listOf(Mood.HAPPY, Mood.ANGRY, Mood.ENERGETIC)
        )
        val activity = activityDetails.toActivity()
        viewModel.preloadActivity(activity)
    } else {
        Log.e("populus", "Failed to copy image for preloading")
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    navigateToAddActivity: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ActivityScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val sharedViewModel: SharedViewModel = (LocalContext.current.applicationContext as MoodApplication).sharedViewModel
    val userMood by sharedViewModel.selectedMood.collectAsState()
    val activityScreenUiState by viewModel.activityScreenUiState.collectAsState()
    var scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
            viewModel = viewModel,
            userMood = userMood
        )
    }
}

@Composable
fun ActivityScreenBody(
    activityList: List<Activity>,
    modifier: Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: ActivityScreenViewModel,
    userMood: Mood?
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    

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
        Spacer(modifier = Modifier.height(128.dp))
        if (userMood != null) {
            Text("Activities for when you're feeiling ${userMood.name}")
        } else {
            Text("All Activities")
        }
        val filteredList = if (userMood != null) {
            activityList.filter { it.suitableMoods.contains(userMood) }
        } else {
            activityList
        }
        ActivityCardList(activityList = filteredList)
    }
    //ActivityList(activityList = activityList, contentPadding = contentPadding)
}

@Composable
fun ActivityList(
    activityList: List<Activity>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding
    ) {
        items(items = activityList, key = { it.id }) { activity ->
            ActivityItem(
                activity = activity,
                modifier = modifier
            )
        }
    }
}

@Composable
fun ActivityItem(
    activity: Activity,
    modifier: Modifier = Modifier
) {
    val painter = rememberAsyncImagePainter(File(activity.imagePath.toString()))
    Card(
        modifier = modifier
    ) {
        Column {
            Text(activity.name)
            Text(activity.info)
            Image(
                painter = painter,
                contentDescription = "Activity Image",
                modifier = Modifier.size(128.dp)
            )
        }
    }
}

/*
@Composable
fun ActivityScreen(navController: NavController){
    Scaffold (
        topBar = {
            MoodCastTopBar()
        },
        bottomBar = {
            BottomNavigationBar(navController)
        },
        floatingActionButton = {
            AddActivityButton { navController.navigate(AppRoutes.ADD_ACTIVITY) }
        }
    ) { innerpadding ->
        Column (modifier = Modifier.padding(innerpadding)){
            Spacer(modifier = Modifier.height(10.dp))
            HeroText(status = WeatherStatus.SUNNY,
                Modifier
                    .fillMaxWidth()
                    .padding())
            Spacer(modifier = Modifier.height(15.dp))
            ActivityCardList()
        }
    }
}
*/

@Composable
fun HeroText(status: WeatherStatus, modifier: Modifier) {
    val text = when (status) {
        WeatherStatus.SUNNY -> "Solfylt og glad?"
        WeatherStatus.RAINY -> "Regn, regn, regn!"
        WeatherStatus.SNOWY -> "Snø, snø, snø!"
        WeatherStatus.CLOUDY -> "Overskyet..."
    }
    val description = when (status) {
        WeatherStatus.SUNNY -> "Ta en tur ut i det fine været, nyt en piknik i parken eller utforsk byen på sykkel for en perfekt dag!"
        WeatherStatus.RAINY -> "Det er en flott dag for innendørsaktiviteter. Hva med å besøke et museum eller se en god film?"
        WeatherStatus.SNOWY -> "Kle deg varmt og nyt vinterlandskapet. Hva med å bygge en snømann?"
        WeatherStatus.CLOUDY -> "En perfekt dag for litt rolig tid. Kanskje lese en bok eller nyte en kaffe på en koselig kafé?"
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityCardList (activityList: List<Activity>) {
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
            items(activityList,) { activity ->
                ActivityCard(activity)
                ShowActivity(activity.toActivityDetails())
            }
        }
    }
}

@Composable
fun ShowActivity(activity: ActivityDetails) {

}

/* Når det gjelder typography, ligger dette i Type filen. Du kan også søke på Typography material design på google for å se hvor store de er! */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityCard(activity: Activity) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
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
    }
}
