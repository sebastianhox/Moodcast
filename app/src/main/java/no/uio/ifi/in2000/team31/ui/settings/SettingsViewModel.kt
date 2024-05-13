package no.uio.ifi.in2000.team31.ui.settings

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.team31.MoodApplication


/*enum class AppTheme {
    LIGHT,
    DARK,
    DEFAULT
}*/

object SettingsDataStore {
    // Add private?
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    val DARK_THEME_ON_KEY = booleanPreferencesKey("dark_theme_on")
    val FAHRENHEIT_ON = booleanPreferencesKey("fahrenheit_on")
}

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val appContainer = (application as MoodApplication).appContainer
    private val settingsRepository = appContainer.settingsRepository

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    private val _isFahrenheit = MutableStateFlow(false)
    val isFahrenheit = _isFahrenheit.asStateFlow()

    init { // Setter theme til det som er lagret i repo
        viewModelScope.launch {
            Log.d("settings", "init SettingsViewModel")

            val darkThemeOn = settingsRepository.getDarkThemeSwitchState()
            _isDarkTheme.value = darkThemeOn

            val fahrenheitOn = settingsRepository.getFahrenheitSwitchState()
            _isFahrenheit.value = fahrenheitOn
        }
    }

    fun onDarkThemeSwitchChange(isChecked: Boolean) { // Kalles når switch blir trykket på
        viewModelScope.launch {
            settingsRepository.updateDarkThemeSwitchState(isChecked) // Oppdaterer repo med ny verdi
            _isDarkTheme.value = settingsRepository.getDarkThemeSwitchState() // Endrer på verdien slik at composable oppdaterer
        }
    }

    fun onFahrenheitSwitchChange(isChecked: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateFahrenheitSwitchState(isChecked)
            _isFahrenheit.value = settingsRepository.getFahrenheitSwitchState()
        }
    }


}