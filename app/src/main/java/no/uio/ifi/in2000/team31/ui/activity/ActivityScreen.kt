package no.uio.ifi.in2000.team31.ui.activity


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import no.uio.ifi.in2000.team31.ui.navigation.BottomNavigationBar

enum class WeatherStatus {
    SUNNY,
    RAINY,
    SNOWY,
    CLOUDY
}
data class Activity(
    val name: String,
    val time: String,
    val suitableLocations: List<Pair<Double, Double>>,
    val image: String,
    val suitableWeather: List<String>,
    val suitableMoods: List<String>
)

@Composable
fun ActivityScreen(navController: NavController){
    Scaffold (
        topBar = {
            MoodCastTopBar()
        },
        bottomBar = {
            BottomNavigationBar(navController)
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

@Composable
fun ActivityCardList () {
    val exampleActivities = listOf(
        //Adding activities
        Activity(
            name = "Morgenjogg",
            time = "30 min",
            suitableLocations = listOf(Pair(59.911491, 10.757933)), //Example coordinates
            image = "https://images.unsplash.com/photo-1649134296132-56606326c566?q=80&w=1932&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90oy1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            suitableWeather = listOf(),
            suitableMoods = listOf()
        ),
        Activity(
            name = "Sykling",
            time = "45 min",
            suitableLocations = listOf(Pair(59.913233, 10.738970)),
            image = "https://images.unsplash.com/photo-1545575439-3261931f52f1?q=80&w=2071&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            suitableWeather = listOf(),
            suitableMoods = listOf()
        ),
        Activity(
            name = "Strandtur",
            time = "30 min",
            suitableLocations = listOf(Pair(59.911491, 10.757933)),
            image = "https://images.unsplash.com/photo-1509233725247-49e657c54213?q=80&w=1949&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            suitableWeather = listOf(),
            suitableMoods = listOf()
        ),
        Activity(
            name = "Iskrem spising",
            time = "20 min",
            suitableLocations = listOf(Pair(59.913233, 10.738970)),
            image = "https://images.unsplash.com/photo-1501443762994-82bd5dace89a?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            suitableWeather = listOf(),
            suitableMoods = listOf()
        ),
        Activity(
            name = "Shopping",
            time = "20 min",
            suitableLocations = listOf(Pair(59.913233, 10.738970)),
            image = "https://images.unsplash.com/photo-1483181994834-aba9fd1e251a?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            suitableWeather = listOf(),
            suitableMoods = listOf()
        ),
        Activity(
            name = "Cafe",
            time = "20 min",
            suitableLocations = listOf(Pair(59.913233, 10.738970)),
            image = "https://plus.unsplash.com/premium_photo-1664970900025-1e3099ca757a?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            suitableWeather = listOf(),
            suitableMoods = listOf()
        ),
        Activity(
            name = "Annen aktivitet",
            time = "20 min",
            suitableLocations = listOf(Pair(59.913233, 10.738970)),
            image = "https://images.unsplash.com/photo-1588534724418-73b75d412ce5?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            suitableWeather = listOf(),
            suitableMoods = listOf()
        ),
        Activity(
            name = "Annen aktivitet",
            time = "20 min",
            suitableLocations = listOf(Pair(59.913233, 10.738970)),
            image = "https://images.unsplash.com/photo-1665686374006-b8f04cf62d57?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
            suitableWeather = listOf(),
            suitableMoods = listOf()
        )
    )
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
            items(exampleActivities) { activity ->
                ActivityCard(activity)
            }
        }
    }
}


/* Når det gjelder typography, ligger dette i Type filen. Du kan også søke på Typography material design på google for å se hvor store de er! */
@Composable
fun ActivityCard(activity: Activity) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = activity.image,
                contentDescription = activity.name,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Text(activity.name, style = MaterialTheme.typography.titleMedium)
            Text(activity.time, style = MaterialTheme.typography.titleMedium)
        }
    }
}

/*
@Preview
@Preview(showBackground = true, uiMode = UI_MODE)

 */


