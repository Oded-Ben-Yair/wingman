package com.wingman.app.keyboard

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wingman.app.data.model.Suggestion
import com.wingman.app.service.ScreenshotProcessor
import com.wingman.app.util.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the custom keyboard
 * Manages suggestions state and listens for new suggestions broadcasts
 */
@HiltViewModel
class KeyboardViewModel @Inject constructor(
    application: Application,
    private val preferencesManager: PreferencesManager
) : AndroidViewModel(application) {
    
    private val _uiState = MutableStateFlow<KeyboardUiState>(KeyboardUiState.Idle)
    val uiState: StateFlow<KeyboardUiState> = _uiState.asStateFlow()
    
    private val suggestionsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ScreenshotProcessor.ACTION_NEW_SUGGESTIONS) {
                loadSuggestions()
            }
        }
    }
    
    init {
        // Register broadcast receiver for new suggestions
        val filter = IntentFilter(ScreenshotProcessor.ACTION_NEW_SUGGESTIONS)
        application.registerReceiver(suggestionsReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        
        // Load existing suggestions
        loadSuggestions()
    }
    
    /**
     * Load suggestions from PreferencesManager
     */
    private fun loadSuggestions() {
        viewModelScope.launch {
            val suggestions = preferencesManager.getSuggestions()
            if (suggestions.isNotEmpty()) {
                _uiState.value = KeyboardUiState.HasSuggestions(suggestions)
            } else {
                _uiState.value = KeyboardUiState.NoSuggestions
            }
        }
    }
    
    /**
     * Show hint for manual upload
     */
    fun showManualUploadHint() {
        _uiState.value = KeyboardUiState.ManualUploadHint
        // Reset to previous state after 3 seconds
        viewModelScope.launch {
            kotlinx.coroutines.delay(3000)
            loadSuggestions()
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().unregisterReceiver(suggestionsReceiver)
    }
}

/**
 * UI state for the keyboard
 */
sealed class KeyboardUiState {
    object Idle : KeyboardUiState()
    object NoSuggestions : KeyboardUiState()
    data class HasSuggestions(val suggestions: List<Suggestion>) : KeyboardUiState()
    object ManualUploadHint : KeyboardUiState()
}
