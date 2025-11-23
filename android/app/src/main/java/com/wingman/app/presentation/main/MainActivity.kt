package com.wingman.app.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.wingman.app.presentation.onboarding.OnboardingScreen
import com.wingman.app.presentation.settings.SettingsActivity
import com.wingman.app.presentation.theme.WingmanTheme
import com.wingman.app.util.PreferencesManager
import com.wingman.app.util.ServiceManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    @Inject
    lateinit var preferencesManager: PreferencesManager
    
    @Inject
    lateinit var serviceManager: ServiceManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            WingmanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showOnboarding by remember { 
                        mutableStateOf(!preferencesManager.isOnboardingCompleted()) 
                    }
                    
                    if (showOnboarding) {
                        OnboardingScreen(
                            onComplete = {
                                preferencesManager.setOnboardingCompleted(true)
                                serviceManager.startScreenshotDetection()
                                showOnboarding = false
                            }
                        )
                    } else {
                        HomeScreen(
                            onNavigateToSettings = {
                                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                            },
                            preferencesManager = preferencesManager
                        )
                    }
                }
            }
        }
    }
}
