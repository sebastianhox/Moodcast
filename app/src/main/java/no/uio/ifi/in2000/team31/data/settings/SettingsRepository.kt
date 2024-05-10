package no.uio.ifi.in2000.team31.data.settings

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import no.uio.ifi.in2000.team31.ui.settings.SettingsDataStore
import no.uio.ifi.in2000.team31.ui.settings.SettingsDataStore.dataStore

class SettingsRepository(private val context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun getDarkThemeSwitchState(): Boolean { // Returnerer verdien lagret i datastore, false hvis ingen verdi er satt
        Log.d("settings", "Getting dark theme switch state")
        return dataStore.data.map { preferences ->
            preferences[SettingsDataStore.DARK_THEME_ON_KEY] ?: false
        }.first()
    }

    suspend fun updateDarkThemeSwitchState(isChecked: Boolean) { // Oppdaterer verdien i datastore
        context.dataStore.edit { preferences ->
            preferences[SettingsDataStore.DARK_THEME_ON_KEY] = isChecked
        }
    }

}