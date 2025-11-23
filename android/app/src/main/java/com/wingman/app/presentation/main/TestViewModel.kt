package com.wingman.app.presentation.main

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wingman.app.data.model.NetworkResult
import com.wingman.app.data.model.Suggestion
import com.wingman.app.data.repository.SuggestionsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for testing API integration
 */
@HiltViewModel
class TestViewModel @Inject constructor(
    private val suggestionsRepository: SuggestionsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow<TestUiState>(TestUiState.Idle)
    val uiState: StateFlow<TestUiState> = _uiState.asStateFlow()
    
    /**
     * Upload a screenshot and get suggestions
     */
    fun uploadScreenshot(bitmap: Bitmap) {
        viewModelScope.launch {
            _uiState.value = TestUiState.Loading
            
            when (val result = suggestionsRepository.uploadScreenshot(bitmap)) {
                is NetworkResult.Success -> {
                    _uiState.value = TestUiState.Success(result.data.suggestions)
                }
                is NetworkResult.Error -> {
                    _uiState.value = TestUiState.Error(result.message)
                }
                is NetworkResult.Loading -> {
                    _uiState.value = TestUiState.Loading
                }
            }
        }
    }
    
    /**
     * Reset the UI state
     */
    fun resetState() {
        _uiState.value = TestUiState.Idle
    }
}

/**
 * UI state for the test screen
 */
sealed class TestUiState {
    object Idle : TestUiState()
    object Loading : TestUiState()
    data class Success(val suggestions: List<Suggestion>) : TestUiState()
    data class Error(val message: String) : TestUiState()
}
