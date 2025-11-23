package com.wingman.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Wingman Application class
 * Entry point for Hilt dependency injection
 */
@HiltAndroidApp
class WingmanApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Application initialization
    }
}
