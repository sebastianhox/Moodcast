package no.uio.ifi.in2000.team31.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.team31.ui.navigation.BottomNavigationBar

@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            SettingItem(title = "Dark theme", description = "Bruk dark theme") {

            }
            SettingItem(title = "Fahrenheit", description = "Bruk fahrenheit") {

            }
            SettingItem(title = "Push varsler", description = "Tillat push varsler") {

            }
            SettingItem(title = "Lokasjon", description = "Tillat lokasjon") {

            }
            SettingItem(title = "Automatisk lokasjon", description = "Last lokasjon automatisk") {

            }
            SettingItem(title = "Eget design", description = "Bruk egendefinert design") {

            }
        }
    }
}

@Composable
fun SettingItem(title: String, description: String, onCheckedChange: (Boolean) -> Unit) {
    var isChecked by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, top = 8.dp, bottom = 8.dp)
        ,
        horizontalArrangement = Arrangement.End
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title)
            Text(description)
        }
        Switch(checked = isChecked, onCheckedChange = {
            isChecked = it
            onCheckedChange(it)
        })
    }
}