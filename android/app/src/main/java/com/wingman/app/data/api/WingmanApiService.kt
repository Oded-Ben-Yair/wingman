package com.wingman.app.data.api

import com.wingman.app.data.model.SuggestionsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * Retrofit API service interface for Wingman backend
 */
interface WingmanApiService {
    
    /**
     * Upload a screenshot and receive AI-generated flirt suggestions
     * 
     * @param screenshot The screenshot image file as multipart data
     * @param sessionId Optional session ID for conversation tracking
     * @return Response containing suggestions and session metadata
     */
    @Multipart
    @POST("/api/v2/trained/analyze-and-generate")
    suspend fun analyzeSuggestions(
        @Part screenshot: MultipartBody.Part,
        @Part("sessionId") sessionId: RequestBody
    ): Response<SuggestionsResponse>
}
