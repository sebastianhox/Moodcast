package no.uio.ifi.in2000.team31.ui.settings

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.data.settings.SettingsRepository


/*enum class AppTheme {
    LIGHT,
    DARK,
    DEFAULT
}*/

object SettingsDataStore {
    // Add private?
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    val DARK_THEME_ON_KEY = booleanPreferencesKey("dark_theme_on")
}

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    init { // Setter theme til det som er lagret i repo
        viewModelScope.launch {
            val darkThemeOn = settingsRepository.getDarkThemeSwitchState()
            _isDarkTheme.value = darkThemeOn
            Log.d("settings", "init SettingsViewModel")
        }
    }

    fun onDarkThemeSwitchChange(isChecked: Boolean) { // Kalles når switch blir trykket på
        viewModelScope.launch {
            settingsRepository.updateDarkThemeSwitchState(isChecked) // Oppdaterer repo med ny verdi
            _isDarkTheme.value = settingsRepository.getDarkThemeSwitchState() // Endrer på verdien slik at composable oppdaterer
        }
    }


}