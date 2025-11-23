package com.wingman.app.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wingman.app.util.PreferencesManager
import com.wingman.app.util.ServiceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the settings screen
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val serviceManager: ServiceManager,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadSettings()
    }
    
    private fun loadSettings() {
        viewModelScope.launch {
            _uiState.value = SettingsUiState(
                isServiceRunning = preferencesManager.isAutoDetectionEnabled()
            )
        }
    }
    
    fun startService() {
        viewModelScope.launch {
            serviceManager.startScreenshotDetection()
            preferencesManager.setAutoDetectionEnabled(true)
            _uiState.value = _uiState.value.copy(isServiceRunning = true)
        }
    }
    
    fun stopService() {
        viewModelScope.launch {
            serviceManager.stopScreenshotDetection()
            preferencesManager.setAutoDetectionEnabled(false)
            _uiState.value = _uiState.value.copy(isServiceRunning = false)
        }
    }
    
    fun clearSuggestions() {
        viewModelScope.launch {
            preferencesManager.clearSuggestions()
        }
    }
    
    fun clearSession() {
        viewModelScope.launch {
            preferencesManager.clearSessionId()
            preferencesManager.clearSuggestions()
        }
    }
}

data class SettingsUiState(
    val isServiceRunning: Boolean = false
)
