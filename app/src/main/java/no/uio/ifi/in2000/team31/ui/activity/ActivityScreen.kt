package no.uio.ifi.in2000.team31.ui.activity

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.firstOrNull
import no.uio.ifi.in2000.team31.container.MoodApplication
import no.uio.ifi.in2000.team31.data.network.Status
import no.uio.ifi.in2000.team31.data.activity.Activity
import no.uio.ifi.in2000.team31.model.populateDatabase
import no.uio.ifi.in2000.team31.ui.navigation.AppRoutes
import no.uio.ifi.in2000.team31.ui.navigation.BottomNavigationBar

enum class WeatherStatus {
    SOL,
    REGN,
    SNO,
    SKYER
}

@Composable
fun ActivityScreen(
    navController: NavController,
    viewModel: ActivityViewModel = viewModel()
) {

    val appContainer = (LocalContext.current.applicationContext as MoodApplication).appContainer
    val sharedViewModel = appContainer.sharedViewModel
    val settingsViewModel = appContainer.settingsViewModel


    val isDarkMode by settingsViewModel.isDarkTheme.collectAsState()
    val userMood by sharedViewModel.moodUIState.collectAsState()
    val currentWeatherStatus by sharedViewModel.weatherUIState.collectAsState()
    val connectionState by sharedViewModel.connectionStatus.collectAsState(
        initial = Status.Unavailable
    )
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
            val color = if (isDarkMode) Color(0xFF002591) else Color(0xFFAAD3FF)
            FloatingActionButton(
                onClick = { navController.navigate(AppRoutes.ADD_ACTIVITY) },
                backgroundColor = color
                ) {
                Icon(
                    Icons.Filled.Add,
                    "Legg til aktivitet")
            }
        }
    ) { innerPadding ->
        val context = LocalContext.current

        LaunchedEffect(key1 = Unit) {
            viewModel.activityRepository.getAllActivitiesStream().firstOrNull()?.let { existingActivity ->
                if (existingActivity.isEmpty()) {
                    Log.d("CheckPopulateDB", "IS EMPTY")
                    populateDatabase(context, viewModel)
                } else {
                    Log.d("CheckPopulateDB", "NOT EMPTY")
                }
            }
        }
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (userMood.selectedMood != null) {
                //Text("Aktiviteter for humør: ${userMood.name}")
                Log.d("moodScreen", "Weatherstatus ${userMood.selectedMood?.name}")
            } else {
                //Text("Alle aktiviteter")
                Log.d("moodScreen", "No mood found")
            }
            if (currentWeatherStatus.currentWeatherStatus != null) {
                //Text("Værstatus: $currentWeather")
                Log.d("moodScreen", "Weatherstatus ${currentWeatherStatus.currentWeatherStatus}")
            } else {
                Text("Værstatus ikke funnet. Si ifra så fikser vi denne bugen?")
                Log.d("moodScreen", "No weatherstatus found")
            }

            if (connectionState == Status.Available) {
                HeroText(
                    status = currentWeatherStatus.currentWeatherStatus,
                    modifier = Modifier.padding(12.dp)
                )
            } else {
                val colorPrim = if (isDarkMode) Color(0xFF002571) else Color(0xFFAAD3FF)
                Text(
                    text = "Klarte ikke å hente data om værforhold\nSjekk din internett tilgang",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(colorPrim)
                        .padding(20.dp)

                )
            }

            val filteredList = activityScreenUiState.activitiesList.filter { activity ->
                val matchesMood = userMood.selectedMood == null || activity.suitableMoods.contains(userMood.selectedMood)
                val matchesWeather =
                    currentWeatherStatus.currentWeatherStatus == null || activity.suitableWeathers.contains(currentWeatherStatus.currentWeatherStatus)
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
}

@Composable
fun HeroText(status: WeatherStatus?, modifier: Modifier) {
    val text = when (status) {
        WeatherStatus.SOL -> "Klar himmel!"
        WeatherStatus.REGN -> "Regn, regn, regn!"
        WeatherStatus.SNO -> "Snø, snø, snø!"
        WeatherStatus.SKYER -> "Overskyet..."
        null -> "Mangler værstatus"
    }
    val description = when (status) {
        WeatherStatus.SOL -> "Ta en tur ut i det fine været, nyt en piknik i parken eller utforsk byen på sykkel for en perfekt dag!"
        WeatherStatus.REGN -> "Det er en flott dag for innendørsaktiviteter. Hva med å besøke et museum eller se en god film?"
        WeatherStatus.SNO -> "Kle deg varmt og nyt vinterlandskapet. Hva med å bygge en snømann?"
        WeatherStatus.SKYER -> "En perfekt dag for litt rolig tid. Kanskje lese en bok eller nyte en kaffe på en koselig kafé?"
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
    viewModel: ActivityViewModel,
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
                            viewModel.deleteActivity(activityToDelete)
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
                Text(
                    activity.info,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
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
            title = { Text("Slette aktivitet?") },
            text = { Text("Er du sikker?") },
            confirmButton = {
                            Button(onClick = {
                                onDeleteClick(activity)
                                showDeleteDialog = false
                            }) {
                                Text("Slett")
                            }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Avbryt")
                }
            }
        )
    }
}