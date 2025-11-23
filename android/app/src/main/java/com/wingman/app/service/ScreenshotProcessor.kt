package com.wingman.app.service

import android.content.Context
import android.content.Intent
import android.util.Log
import com.wingman.app.data.model.NetworkResult
import com.wingman.app.data.repository.SuggestionsRepository
import com.wingman.app.util.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Processes detected screenshots: compress, upload, save, and broadcast
 */
@Singleton
class ScreenshotProcessor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val suggestionsRepository: SuggestionsRepository,
    private val preferencesManager: PreferencesManager
) {
    
    companion object {
        private const val TAG = "ScreenshotProcessor"
        const val ACTION_NEW_SUGGESTIONS = "com.wingman.NEW_SUGGESTIONS"
        const val EXTRA_SUGGESTION_COUNT = "suggestion_count"
    }
    
    private val scope = CoroutineScope(Dispatchers.IO)
    
    /**
     * Process a screenshot: upload to backend and handle response
     */
    fun processScreenshot(filePath: String) {
        Log.i(TAG, "Processing screenshot: $filePath")
        
        scope.launch {
            try {
                // Get current session ID (or null for new session)
                val sessionId = preferencesManager.getCurrentSessionId()
                
                // Upload screenshot to backend
                when (val result = suggestionsRepository.uploadScreenshotFromPath(filePath, sessionId)) {
                    is NetworkResult.Success -> {
                        val response = result.data
                        Log.i(TAG, "Received ${response.suggestions.size} suggestions")
                        
                        // Save session ID
                        preferencesManager.saveSessionId(response.session.id)
                        
                        // Save suggestions
                        preferencesManager.saveSuggestions(response.suggestions)
                        
                        // Broadcast new suggestions event
                        broadcastNewSuggestions(response.suggestions.size)
                        
                        Log.i(TAG, "Screenshot processed successfully")
                    }
                    
                    is NetworkResult.Error -> {
                        Log.e(TAG, "Failed to process screenshot: ${result.message}")
                        // Could show notification to user about error
                    }
                    
                    is NetworkResult.Loading -> {
                        // Should not happen in this flow
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing screenshot", e)
            }
        }
    }
    
    /**
     * Broadcast that new suggestions are available
     */
    private fun broadcastNewSuggestions(count: Int) {
        val intent = Intent(ACTION_NEW_SUGGESTIONS).apply {
            putExtra(EXTRA_SUGGESTION_COUNT, count)
        }
        context.sendBroadcast(intent)
        Log.d(TAG, "Broadcast sent: $count new suggestions")
    }
}
