package no.uio.ifi.in2000.team31.ui.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


enum class AppTheme {
    LIGHT,
    DARK,
    DEFAULT
}

class SettingsViewModel() {
    val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()
    fun toggleDarkTheme() {
        _isDarkTheme.value =  !(_isDarkTheme.value)
    }

}