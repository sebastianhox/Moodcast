package no.uio.ifi.in2000.team31.ui.alert

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class Alert(
    val title: String?,
    val area: String?,
    val type: String?,
    val awarenessColor: String?,
    val endDate: String?,
    val instruction: String?
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertScreen(navController: NavController, alertViewModel: AlertViewModel = viewModel()) {
    alertViewModel.startAlertUpdates()
    val weatherAlertUIState by alertViewModel.weatherAlertUIState.collectAsState()
    val features = weatherAlertUIState.features

    Scaffold(
        topBar = {
            AlertTopAppBar(navController)
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Spacer(modifier = Modifier.height(15.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (features != null) {
                    itemsIndexed(features) { _, feature ->
                        val dataMap = mutableMapOf<String, String?>()
                        feature.properties.forEach { entry ->
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
                                endDate = dataMap["eventEndingTime"],
                                instruction = dataMap["instruction"]
                            ),
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
        title = {
            Text(
                "MoodCast",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(start = 24.dp)
                    .fillMaxWidth(),
            )
        },
        actions = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.Close, "close")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 1.dp)
            .shadow(3.dp, RoundedCornerShape(bottomEnd = 4.dp, bottomStart = 4.dp))
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String?): String {
    return if (dateString != null) {
        try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM-yyyy HH:mm")
            val date = ZonedDateTime.parse(dateString)
            date.format(formatter)
        } catch (e: Exception) {
            "Ukjent dato"
        }
    } else {
        "Ukjent"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AlertCard(alert: Alert) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(
            containerColor = when (alert.awarenessColor.toString()) {
                "Yellow" -> Color(0xFFFFF176).copy(alpha = 0.9f)
                "Orange" -> Color(0xFFFFA726).copy(alpha = 0.9f)
                "Red" -> Color(0xFFE57373).copy(alpha = 0.9f)
                else -> Color.LightGray.copy(alpha = 0.9f)
            }
        )
        ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start

            ) {
                // Tittel
                Text(
                    text = "${alert.title?.split(",")?.firstOrNull()}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Område
                Text(
                    text = "${alert.area}",
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Gjelder til (vises kun hvis farevarselet faktisk har et final tidspunkt)
                if (formatDate(alert.endDate) != "Ukjent") {
                    Text(
                        text = "Gjelder til ${formatDate(alert.endDate)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Instruksjoner
                Text(
                    text = alert.instruction.toString(),
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }

