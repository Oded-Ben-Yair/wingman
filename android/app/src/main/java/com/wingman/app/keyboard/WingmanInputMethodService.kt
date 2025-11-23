package com.wingman.app.keyboard

import android.inputmethodservice.InputMethodService
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import dagger.hilt.android.AndroidEntryPoint

/**
 * Custom Input Method Service (Keyboard) for Wingman
 * Displays AI-generated suggestions as tappable chips
 */
@AndroidEntryPoint
class WingmanInputMethodService : InputMethodService(), 
    ViewModelStoreOwner,
    SavedStateRegistryOwner {
    
    private val viewModelStore = ViewModelStore()
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    
    private lateinit var keyboardViewModel: KeyboardViewModel
    
    override fun onCreate() {
        super.onCreate()
        savedStateRegistryController.performRestore(null)
        
        // Initialize ViewModel
        keyboardViewModel = ViewModelProvider(this)[KeyboardViewModel::class.java]
    }
    
    override fun onCreateInputView(): View {
        // Create Compose view for keyboard UI
        return ComposeView(this).apply {
            setContent {
                WingmanKeyboardUI(
                    viewModel = keyboardViewModel,
                    onSuggestionTap = { suggestion ->
                        insertText(suggestion.text)
                    },
                    onKeyTap = { key ->
                        handleKeyPress(key)
                    },
                    onManualUpload = {
                        // Manual upload will be handled in the main app
                        // For now, just show a message
                        keyboardViewModel.showManualUploadHint()
                    }
                )
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
    }
    
    override fun getViewModelStore(): ViewModelStore = viewModelStore
    
    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry
    
    /**
     * Insert text into the current input field
     */
    private fun insertText(text: String) {
        currentInputConnection?.let { ic ->
            ic.commitText(text, 1)
        }
    }
    
    /**
     * Handle keyboard key presses
     */
    private fun handleKeyPress(key: String) {
        currentInputConnection?.let { ic ->
            when (key) {
                "BACKSPACE" -> {
                    ic.deleteSurroundingText(1, 0)
                }
                "ENTER" -> {
                    ic.commitText("\n", 1)
                }
                "SPACE" -> {
                    ic.commitText(" ", 1)
                }
                else -> {
                    ic.commitText(key, 1)
                }
            }
        }
    }
}
