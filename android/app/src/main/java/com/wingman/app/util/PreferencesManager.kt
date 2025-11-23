package com.wingman.app.util

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.wingman.app.data.model.Suggestion
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager for storing and retrieving data from SharedPreferences
 */
@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context,
    private val moshi: Moshi
) {
    
    companion object {
        private const val PREFS_NAME = "wingman_prefs"
        private const val KEY_SUGGESTIONS = "suggestions"
        private const val KEY_SESSION_ID = "session_id"
        private const val KEY_AUTO_DETECTION_ENABLED = "auto_detection_enabled"
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
    }
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private val suggestionsAdapter = moshi.adapter<List<Suggestion>>(
        Types.newParameterizedType(List::class.java, Suggestion::class.java)
    )
    
    /**
     * Save suggestions to SharedPreferences
     */
    fun saveSuggestions(suggestions: List<Suggestion>) {
        val json = suggestionsAdapter.toJson(suggestions)
        prefs.edit().putString(KEY_SUGGESTIONS, json).apply()
    }
    
    /**
     * Get saved suggestions from SharedPreferences
     */
    fun getSuggestions(): List<Suggestion> {
        val json = prefs.getString(KEY_SUGGESTIONS, null) ?: return emptyList()
        return suggestionsAdapter.fromJson(json) ?: emptyList()
    }
    
    /**
     * Clear all saved suggestions
     */
    fun clearSuggestions() {
        prefs.edit().remove(KEY_SUGGESTIONS).apply()
    }
    
    /**
     * Save session ID
     */
    fun saveSessionId(sessionId: String) {
        prefs.edit().putString(KEY_SESSION_ID, sessionId).apply()
    }
    
    /**
     * Get current session ID
     */
    fun getCurrentSessionId(): String? {
        return prefs.getString(KEY_SESSION_ID, null)
    }
    
    /**
     * Clear session ID (start new conversation)
     */
    fun clearSessionId() {
        prefs.edit().remove(KEY_SESSION_ID).apply()
    }
    
    /**
     * Enable or disable automatic screenshot detection
     */
    fun setAutoDetectionEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_DETECTION_ENABLED, enabled).apply()
    }
    
    /**
     * Check if automatic screenshot detection is enabled
     */
    fun isAutoDetectionEnabled(): Boolean {
        return prefs.getBoolean(KEY_AUTO_DETECTION_ENABLED, true)
    }
    
    /**
     * Mark onboarding as completed
     */
    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
    }
    
    /**
     * Check if onboarding has been completed
     */
    fun isOnboardingCompleted(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }
}
