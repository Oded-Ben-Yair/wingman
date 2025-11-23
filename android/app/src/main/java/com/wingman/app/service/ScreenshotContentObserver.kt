package com.wingman.app.service

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ContentObserver that monitors MediaStore for new screenshots
 */
class ScreenshotContentObserver(
    private val context: Context,
    private val onScreenshotDetected: (String) -> Unit
) : ContentObserver(Handler(Looper.getMainLooper())) {
    
    companion object {
        private const val TAG = "ScreenshotObserver"
        private const val DEBOUNCE_DELAY_MS = 2000L
        
        // Common screenshot path patterns across different manufacturers
        private val SCREENSHOT_PATHS = listOf(
            "/screenshots/",
            "/screenshot/",
            "/screencapture/",
            "/screen capture/",
            "/dcim/screenshots/",
            "/pictures/screenshots/",
            "/images/screenshots/"
        )
    }
    
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private var lastDetectionTime = 0L
    private var lastDetectedPath: String? = null
    
    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        
        uri ?: return
        
        // Debounce: Ignore if detected within the last 2 seconds
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastDetectionTime < DEBOUNCE_DELAY_MS) {
            Log.d(TAG, "Debounced: Too soon after last detection")
            return
        }
        
        scope.launch {
            // Small delay to ensure file is fully written
            delay(500)
            
            val screenshotPath = getScreenshotPath(uri)
            if (screenshotPath != null && screenshotPath != lastDetectedPath) {
                Log.i(TAG, "Screenshot detected: $screenshotPath")
                lastDetectionTime = currentTime
                lastDetectedPath = screenshotPath
                onScreenshotDetected(screenshotPath)
            }
        }
    }
    
    /**
     * Get the file path from MediaStore URI and verify it's a screenshot
     */
    private fun getScreenshotPath(uri: Uri): String? {
        var cursor: Cursor? = null
        try {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED
            )
            
            cursor = context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DATE_ADDED} DESC"
            )
            
            if (cursor != null && cursor.moveToFirst()) {
                val dataIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val nameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                val dateIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                
                if (dataIndex != -1) {
                    val path = cursor.getString(dataIndex)
                    val name = if (nameIndex != -1) cursor.getString(nameIndex) else ""
                    val dateAdded = if (dateIndex != -1) cursor.getLong(dateIndex) else 0L
                    
                    // Check if the image was added very recently (within last 5 seconds)
                    val currentTime = System.currentTimeMillis() / 1000
                    if (currentTime - dateAdded > 5) {
                        Log.d(TAG, "Image too old: $path")
                        return null
                    }
                    
                    // Check if path or name contains screenshot indicators
                    if (isScreenshot(path, name)) {
                        return path
                    } else {
                        Log.d(TAG, "Not a screenshot: $path")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting screenshot path", e)
        } finally {
            cursor?.close()
        }
        
        return null
    }
    
    /**
     * Check if the file path or name indicates it's a screenshot
     */
    private fun isScreenshot(path: String, name: String): Boolean {
        val lowerPath = path.lowercase()
        val lowerName = name.lowercase()
        
        // Check path patterns
        val pathMatches = SCREENSHOT_PATHS.any { lowerPath.contains(it) }
        
        // Check filename patterns
        val nameMatches = lowerName.startsWith("screenshot") ||
                         lowerName.startsWith("screen_") ||
                         lowerName.startsWith("screencap") ||
                         lowerName.startsWith("capture_")
        
        return pathMatches || nameMatches
    }
}
