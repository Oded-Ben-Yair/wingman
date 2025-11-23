package com.wingman.app.keyboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wingman.app.data.model.Suggestion

/**
 * Main composable for the Wingman keyboard UI
 */
@Composable
fun WingmanKeyboardUI(
    viewModel: KeyboardViewModel,
    onSuggestionTap: (Suggestion) -> Unit,
    onKeyTap: (String) -> Unit,
    onManualUpload: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2C2C2E))
            .padding(vertical = 8.dp)
    ) {
        // Suggestions Row
        when (val state = uiState) {
            is KeyboardUiState.HasSuggestions -> {
                SuggestionsRow(
                    suggestions = state.suggestions,
                    onSuggestionTap = onSuggestionTap,
                    onManualUpload = onManualUpload
                )
            }
            is KeyboardUiState.NoSuggestions -> {
                EmptySuggestionsRow(onManualUpload = onManualUpload)
            }
            is KeyboardUiState.ManualUploadHint -> {
                ManualUploadHintRow()
            }
            else -> {
                EmptySuggestionsRow(onManualUpload = onManualUpload)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Keyboard Layout
        SimpleKeyboardLayout(onKeyTap = onKeyTap)
    }
}

/**
 * Row of suggestion chips
 */
@Composable
fun SuggestionsRow(
    suggestions: List<Suggestion>,
    onSuggestionTap: (Suggestion) -> Unit,
    onManualUpload: () -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Manual upload button
        item {
            IconButton(
                onClick = onManualUpload,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF3A3A3C))
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Upload screenshot",
                    tint = Color.White
                )
            }
        }
        
        // Suggestion chips
        items(suggestions) { suggestion ->
            SuggestionChip(
                suggestion = suggestion,
                onClick = { onSuggestionTap(suggestion) }
            )
        }
    }
}

/**
 * Empty state when no suggestions available
 */
@Composable
fun EmptySuggestionsRow(onManualUpload: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onManualUpload,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF3A3A3C))
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = "Upload screenshot",
                tint = Color.White
            )
        }
        
        Text(
            text = "Take a screenshot to get AI suggestions",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Hint for manual upload
 */
@Composable
fun ManualUploadHintRow() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF007AFF))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Open the Wingman app to manually upload a screenshot",
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Individual suggestion chip
 */
@Composable
fun SuggestionChip(
    suggestion: Suggestion,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .widthIn(min = 150.dp, max = 300.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFF6B6B),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = suggestion.tone.uppercase(),
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${(suggestion.confidence * 100).toInt()}%",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 10.sp
                )
            }
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Text(
                text = suggestion.text,
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                maxLines = 3
            )
        }
    }
}

/**
 * Simple QWERTY keyboard layout
 */
@Composable
fun SimpleKeyboardLayout(onKeyTap: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Row 1: Q W E R T Y U I O P
        KeyboardRow(
            keys = listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
            onKeyTap = onKeyTap
        )
        
        // Row 2: A S D F G H J K L
        KeyboardRow(
            keys = listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
            onKeyTap = onKeyTap,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        
        // Row 3: Z X C V B N M [BACKSPACE]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            listOf("Z", "X", "C", "V", "B", "N", "M").forEach { key ->
                KeyButton(
                    text = key,
                    onClick = { onKeyTap(key) },
                    modifier = Modifier.weight(1f)
                )
            }
            KeyButton(
                text = "⌫",
                onClick = { onKeyTap("BACKSPACE") },
                modifier = Modifier.weight(1.5f)
            )
        }
        
        // Row 4: [SPACE] [ENTER]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            KeyButton(
                text = "SPACE",
                onClick = { onKeyTap("SPACE") },
                modifier = Modifier.weight(3f)
            )
            KeyButton(
                text = "↵",
                onClick = { onKeyTap("ENTER") },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Row of keyboard keys
 */
@Composable
fun KeyboardRow(
    keys: List<String>,
    onKeyTap: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        keys.forEach { key ->
            KeyButton(
                text = key,
                onClick = { onKeyTap(key) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Individual keyboard key button
 */
@Composable
fun KeyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(42.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFF3A3A3C)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
