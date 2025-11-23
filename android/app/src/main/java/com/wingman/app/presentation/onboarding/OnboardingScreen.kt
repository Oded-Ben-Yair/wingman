package com.wingman.app.presentation.onboarding

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wingman.app.R
import com.wingman.app.util.PermissionsHelper
import com.wingman.app.util.ServiceManager

/**
 * Onboarding screen that guides users through setup
 */
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    
    var currentStep by remember { mutableStateOf(0) }
    
    val steps = listOf(
        OnboardingStep.Welcome,
        OnboardingStep.Permissions,
        OnboardingStep.KeyboardSetup,
        OnboardingStep.Complete
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (steps[currentStep]) {
            OnboardingStep.Welcome -> {
                WelcomeStep(
                    onNext = { currentStep++ }
                )
            }
            OnboardingStep.Permissions -> {
                PermissionsStep(
                    onNext = { currentStep++ },
                    onRequestPermissions = {
                        activity?.let {
                            PermissionsHelper.requestStoragePermission(it)
                            PermissionsHelper.requestNotificationPermission(it)
                            PermissionsHelper.requestDisableBatteryOptimization(it)
                        }
                    }
                )
            }
            OnboardingStep.KeyboardSetup -> {
                KeyboardSetupStep(
                    onNext = { currentStep++ },
                    onOpenKeyboardSettings = {
                        val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                        context.startActivity(intent)
                    }
                )
            }
            OnboardingStep.Complete -> {
                CompleteStep(
                    onFinish = onComplete
                )
            }
        }
    }
}

@Composable
fun WelcomeStep(onNext: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "🚀",
            style = MaterialTheme.typography.displayLarge
        )
        
        Text(
            text = "Welcome to Wingman",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Your AI-powered dating assistant that helps you craft the perfect messages",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FeatureItem("📸 Automatic screenshot detection")
                FeatureItem("🤖 AI-powered suggestions")
                FeatureItem("⌨️ Custom keyboard integration")
                FeatureItem("🔥 Spicy, flirty, and playful tones")
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Started")
        }
    }
}

@Composable
fun PermissionsStep(
    onNext: () -> Unit,
    onRequestPermissions: () -> Unit
) {
    val context = LocalContext.current
    val hasAllPermissions = PermissionsHelper.hasAllPermissions(context)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "🔐",
            style = MaterialTheme.typography.displayLarge
        )
        
        Text(
            text = "Required Permissions",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Wingman needs a few permissions to work its magic",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PermissionCard(
                icon = "📁",
                title = "Storage Access",
                description = "To detect and read screenshots",
                granted = PermissionsHelper.hasStoragePermission(context)
            )
            
            PermissionCard(
                icon = "🔔",
                title = "Notifications",
                description = "To show the background service",
                granted = PermissionsHelper.hasNotificationPermission(context)
            )
            
            PermissionCard(
                icon = "🔋",
                title = "Battery Optimization",
                description = "To keep the service running",
                granted = PermissionsHelper.isBatteryOptimizationDisabled(context)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        if (hasAllPermissions) {
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }
        } else {
            Button(
                onClick = onRequestPermissions,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Grant Permissions")
            }
        }
    }
}

@Composable
fun KeyboardSetupStep(
    onNext: () -> Unit,
    onOpenKeyboardSettings: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "⌨️",
            style = MaterialTheme.typography.displayLarge
        )
        
        Text(
            text = "Enable Wingman Keyboard",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Follow these steps to enable the Wingman keyboard",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StepItem("1", "Tap 'Open Keyboard Settings' below")
                StepItem("2", "Find 'Wingman' in the list")
                StepItem("3", "Toggle it ON")
                StepItem("4", "Return to this app")
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onOpenKeyboardSettings,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Open Keyboard Settings")
        }
        
        TextButton(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("I've enabled it")
        }
    }
}

@Composable
fun CompleteStep(onFinish: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "✅",
            style = MaterialTheme.typography.displayLarge
        )
        
        Text(
            text = "You're All Set!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "Wingman is now ready to help you with your dating conversations",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "How it works:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                StepItem("1", "Take a screenshot in your dating app")
                StepItem("2", "Wingman detects it automatically")
                StepItem("3", "Switch to Wingman keyboard")
                StepItem("4", "Tap a suggestion to send it")
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onFinish,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start Using Wingman")
        }
    }
}

@Composable
fun FeatureItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "✓",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun PermissionCard(
    icon: String,
    title: String,
    description: String,
    granted: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (granted) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineMedium
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = if (granted) "✓" else "○",
                style = MaterialTheme.typography.headlineSmall,
                color = if (granted) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StepItem(number: String, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.primary
        ) {
            Text(
                text = number,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

sealed class OnboardingStep {
    object Welcome : OnboardingStep()
    object Permissions : OnboardingStep()
    object KeyboardSetup : OnboardingStep()
    object Complete : OnboardingStep()
}
