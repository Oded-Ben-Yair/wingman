package com.wingman.app.util

import android.content.Context
import android.content.Intent
import android.os.Build
import com.wingman.app.service.ScreenshotDetectionService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager for controlling the ScreenshotDetectionService
 */
@Singleton
class ServiceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    /**
     * Start the screenshot detection service
     */
    fun startScreenshotDetection() {
        val intent = Intent(context, ScreenshotDetectionService::class.java).apply {
            action = ScreenshotDetectionService.ACTION_START
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
    
    /**
     * Stop the screenshot detection service
     */
    fun stopScreenshotDetection() {
        val intent = Intent(context, ScreenshotDetectionService::class.java).apply {
            action = ScreenshotDetectionService.ACTION_STOP
        }
        context.startService(intent)
    }
    
    /**
     * Check if the service is running
     * Note: This is a simplified check. In production, you might want to use
     * ActivityManager to check if the service is actually running.
     */
    fun isServiceRunning(): Boolean {
        // This would require checking ActivityManager.getRunningServices()
        // For now, we'll rely on the PreferencesManager setting
        return true
    }
}
