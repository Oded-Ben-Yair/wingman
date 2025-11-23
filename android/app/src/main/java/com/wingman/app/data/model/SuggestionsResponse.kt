package com.wingman.app.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data model for the API response containing suggestions and session info
 */
@JsonClass(generateAdapter = true)
data class SuggestionsResponse(
    @Json(name = "suggestions")
    val suggestions: List<Suggestion>,
    
    @Json(name = "session")
    val session: Session
)
