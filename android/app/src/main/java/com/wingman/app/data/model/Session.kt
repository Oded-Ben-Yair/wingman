package com.wingman.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data model for session metadata
 */
@JsonClass(generateAdapter = true)
data class Session(
    @Json(name = "id")
    val id: String,
    
    @Json(name = "createdAt")
    val createdAt: String
)
