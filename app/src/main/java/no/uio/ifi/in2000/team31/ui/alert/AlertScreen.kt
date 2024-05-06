package no.uio.ifi.in2000.team31.ui.alert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.uio.ifi.in2000.team31.ui.activity.MoodCastTopBar

data class Alert(
    val title: String?,
    val area: String?,
    val type: String?,
    val awarenessColor: String?,
    val description: String?,
    val instruction: String?
)
@Composable
fun AlertScreen(navController: NavController, alertViewModel: AlertViewModel = viewModel()) {
    alertViewModel.startAlertUpdates()
    val weatherAlertUIState by alertViewModel.weatherAlertUIState.collectAsState()
    val features = weatherAlertUIState.features

    Scaffold(
        topBar = {
            AlertTopAppBar(navController)
        }
    ) { innerpadding ->
        Column(modifier = Modifier.padding(innerpadding)) {
            Spacer(modifier = Modifier.height(15.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                    if (features != null) {
                        items(features) { feature ->
                            val dataMap = mutableMapOf<String, String?>()
                            feature.properties.forEach{entry ->
                                if (entry.value !is kotlinx.serialization.json.JsonArray) {
                                    dataMap[entry.key] = feature.getStringProperty(entry.key)
                                }
                            }

                            AlertCard(
                                alert = Alert(
                                    title = dataMap["title"],
                                    area = dataMap["area"],
                                    type = dataMap["eventAwarenessName"],
                                    awarenessColor = dataMap["riskMatrixColor"],
                                    description = dataMap["description"],
                                    instruction = dataMap["instruction"]
                                )
                            )
                        }
                    }
                }
            }
        }
    }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertTopAppBar(navController: NavController) {
    TopAppBar(
        title = { Text(
            "MoodCast",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(end = 33.dp)
                .fillMaxWidth(),
        )},
        actions = {
                  IconButton(onClick ={ navController.popBackStack() }) {
                      Icon(Icons.Filled.Close,"close")
                  }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp)
            .shadow(3.dp, RoundedCornerShape(bottomEnd = 4.dp, bottomStart = 4.dp))
    )

}
@Composable
fun AlertCard(alert: Alert) {
    Column {

        Card(
            modifier = Modifier.padding(horizontal = 10.dp),
            colors = CardDefaults.cardColors(
                containerColor =
                when {
                    alert.awarenessColor.toString() == "Yellow" -> Color.Yellow
                    alert.awarenessColor.toString() == "Orange" -> Color.Magenta
                    alert.awarenessColor.toString() == "Red" -> Color.Red
                    else -> Color.Transparent
                }
            )
        ) {
            Column(
                Modifier.padding(8.dp)
            ) {
                Text(alert.title.toString(), style = MaterialTheme.typography.titleLarge)
                Text(alert.area.toString(), style = MaterialTheme.typography.titleMedium)
                Text(alert.instruction.toString(), style = MaterialTheme.typography.bodyMedium)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

