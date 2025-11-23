package com.wingman.app.presentation.settings

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wingman.app.util.PermissionsHelper

/**
 * Settings screen for configuring Wingman
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Service Control Section
            SectionHeader("Service")
            
            SettingsItem(
                icon = Icons.Default.PlayArrow,
                title = "Screenshot Detection",
                description = if (uiState.isServiceRunning) "Running" else "Stopped",
                trailing = {
                    Switch(
                        checked = uiState.isServiceRunning,
                        onCheckedChange = { enabled ->
                            if (enabled) {
                                viewModel.startService()
                            } else {
                                viewModel.stopService()
                            }
                        }
                    )
                }
            )
            
            Divider()
            
            // Permissions Section
            SectionHeader("Permissions")
            
            SettingsItem(
                icon = Icons.Default.Image,
                title = "Storage Access",
                description = if (PermissionsHelper.hasStoragePermission(context)) 
                    "Granted" else "Not granted",
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = android.net.Uri.parse("package:${context.packageName}")
                    context.startActivity(intent)
                }
            )
            
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                description = if (PermissionsHelper.hasNotificationPermission(context)) 
                    "Granted" else "Not granted",
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = android.net.Uri.parse("package:${context.packageName}")
                    context.startActivity(intent)
                }
            )
            
            SettingsItem(
                icon = Icons.Default.BatteryChargingFull,
                title = "Battery Optimization",
                description = if (PermissionsHelper.isBatteryOptimizationDisabled(context)) 
                    "Disabled" else "Enabled",
                onClick = {
                    val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                    context.startActivity(intent)
                }
            )
            
            Divider()
            
            // Keyboard Section
            SectionHeader("Keyboard")
            
            SettingsItem(
                icon = Icons.Default.Keyboard,
                title = "Keyboard Settings",
                description = "Enable or disable Wingman keyboard",
                onClick = {
                    val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)
                    context.startActivity(intent)
                }
            )
            
            Divider()
            
            // Data Section
            SectionHeader("Data")
            
            SettingsItem(
                icon = Icons.Default.Delete,
                title = "Clear Suggestions",
                description = "Remove all saved suggestions",
                onClick = {
                    viewModel.clearSuggestions()
                }
            )
            
            SettingsItem(
                icon = Icons.Default.Refresh,
                title = "New Conversation",
                description = "Start a fresh conversation session",
                onClick = {
                    viewModel.clearSession()
                }
            )
            
            Divider()
            
            // About Section
            SectionHeader("About")
            
            SettingsItem(
                icon = Icons.Default.Info,
                title = "Version",
                description = "1.0.0 (Build 1)"
            )
            
            SettingsItem(
                icon = Icons.Default.PrivacyTip,
                title = "Privacy Policy",
                description = "View our privacy policy",
                onClick = {
                    // Open privacy policy URL
                }
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        trailing?.invoke()
    }
}
