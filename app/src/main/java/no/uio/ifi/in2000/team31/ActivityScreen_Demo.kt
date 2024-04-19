package no.uio.ifi.in2000.mariufe.mariufe.design_demo.ui.theme

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ActivityScreen(){
    Scaffold (topBar = {
        ActivityTopScreen()
    }){ innerpadding ->
        Column (modifier = Modifier.padding(innerpadding)){
            Text(text = "Hello, world")

            Spacer(modifier = Modifier.height(32.dp))

            HeroText(status = WeatherStatus.SUNNY, modifier.fillMaxWidth().padding()) //Sette egen padding for å se!


            Spacer(modifier = Modifier.height(16.dp))

            ActivityCardList()
        }

    }
}


@Composable //Hva er en herotext???????
fun HeroText(status: WeatherStatus, modifier: Modifier) { //Sette egen padding!
    val text = when (status) {
        WeatherStatus.SUNNY -> "Solfylt og glad?"
        /*
        FORTSETTER
         */

    }

    val description = when (status) {
        WeatherStatus.SUNNY -> "AKTIVITET"
    }

    Column (modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Text(text, style = MaterialTheme, Typography.titleLarge)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityTopScreen(){
    TopAppBar(modifier = Modifier,shadow(4.dp), title = { Text(text = "MoodCast", style = MaterialTheme.typography.titleLarge, TextWeight = SEMIBOLD, textAlign = TextAlign.Center, modifier = Modifier,fillmaxWidth().padding(end = 16.dp))})
    navigationIcon = {IconButton(onClick = {}) {Icon(Icons.Default.Menu, contentDescription = null)}}
}


fun ActivityCardList () {
    val exampleActivity = listOf(
        Activity(
            name = "Morgenjogg",
            desctiption = "Start dagen med en....",
            time = "30 min",
            suitableLocations = listOf(Pair(KORDINATER)),
            image = "LINK AV BILDE ADDRESSE"
        )
    )

    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)){
        Text(text = "Anbefalte aktivtiteter", style = MaterialTheme.typography.titleMedium, FontWeight = ) {
            LazyVerticalGrid(columns = GridCells.Fixed(2)) { //Scrollbart, men to kolonner!
                items(exampleActivites) { activity ->//En liste med forskjellige aktiviteter
                    ActivityCard(activity)
                }

            }
        }
    }
}

/*
Når det gjelder typography, ligger dette i Type filen. Du kan også søke på Typography material design på google
for å se hvor store de er!


 */

@Composable
fun ActivityCard(activity: Activity, onCardClicked: () -> {}) {

    Card(
        onClick = onCardClicked, colors = CardDefaults.cardColors(
            containerColor = Color.Transparent //Vil bare ha kortet
        )
    ) {

        //Må adde dependency??? Internett bla. !!!!
        AsyncImage(
            model = activity.image, contentDescription = activity,name, modifier = Modifier.fillMaxWidth().clip(roundedCornershape(8.dp)), contentScale = ContentScale.Crop , modifier = Modifier.height(100.dp).fillmaxwidth().clip(roundedCorner)
        )

        column(
            modifier.padding(Horixonbtal = 8.dp)
        )

        Text(Acitvity.name, style = materialthem.typography.titleMedium) //EN TIL MEN BODYMEDIUM


    }
}

data class Activity(
    val name: String,
    val desctiption: String,
    val time: String,
    val suitableLocations: List<Pair<Double, Double>>,//ELLER BARE STEDSNAVN KOMMER ANN PÅ HVA VI VELGER Å GJØRE
    val image: String, //URL til et bildet | søker på unsplash, tar selve bilde lenke og lagre det inne her!
)


@Preview
@Preview(showBackground = true, uiMode = UI_MODE)


enum class WeatherStatus {
    SUNNY
}