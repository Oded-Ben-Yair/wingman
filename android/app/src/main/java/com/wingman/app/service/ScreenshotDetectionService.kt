package com.wingman.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import com.wingman.app.R
import com.wingman.app.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Foreground service that monitors for new screenshots
 */
@AndroidEntryPoint
class ScreenshotDetectionService : Service() {
    
    companion object {
        private const val TAG = "ScreenshotService"
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "screenshot_detection"
        private const val CHANNEL_NAME = "Screenshot Detection"
        
        const val ACTION_START = "com.wingman.START_DETECTION"
        const val ACTION_STOP = "com.wingman.STOP_DETECTION"
    }
    
    @Inject
    lateinit var screenshotProcessor: ScreenshotProcessor
    
    private var contentObserver: ScreenshotContentObserver? = null
    
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service created")
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> startDetection()
            ACTION_STOP -> stopDetection()
        }
        
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopDetection()
        Log.i(TAG, "Service destroyed")
    }
    
    /**
     * Start screenshot detection
     */
    private fun startDetection() {
        Log.i(TAG, "Starting screenshot detection")
        
        // Start as foreground service
        startForeground(NOTIFICATION_ID, createNotification())
        
        // Register ContentObserver
        if (contentObserver == null) {
            contentObserver = ScreenshotContentObserver(
                context = this,
                onScreenshotDetected = { path ->
                    Log.i(TAG, "Screenshot detected, processing: $path")
                    screenshotProcessor.processScreenshot(path)
                }
            )
            
            contentResolver.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                contentObserver!!
            )
            
            Log.i(TAG, "ContentObserver registered")
        }
    }
    
    /**
     * Stop screenshot detection
     */
    private fun stopDetection() {
        Log.i(TAG, "Stopping screenshot detection")
        
        // Unregister ContentObserver
        contentObserver?.let {
            contentResolver.unregisterContentObserver(it)
            contentObserver = null
            Log.i(TAG, "ContentObserver unregistered")
        }
        
        // Stop foreground service
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }
    
    /**
     * Create notification channel for Android O+
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.service_channel_description)
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    /**
     * Create persistent notification for foreground service
     */
    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.service_notification_title))
            .setContentText(getString(R.string.service_notification_description))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }
}
