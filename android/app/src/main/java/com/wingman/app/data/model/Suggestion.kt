package com.wingman.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data model for a single AI-generated flirt suggestion
 */
@JsonClass(generateAdapter = true)
data class Suggestion(
    @Json(name = "text")
    val text: String,
    
    @Json(name = "tone")
    val tone: String,
    
    @Json(name = "confidence")
    val confidence: Double
)
