package com.wingman.app.data.repository

import android.graphics.Bitmap
import com.wingman.app.data.api.WingmanApiService
import com.wingman.app.data.model.NetworkResult
import com.wingman.app.data.model.SuggestionsResponse
import com.wingman.app.util.ImageCompressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing suggestions API calls
 */
@Singleton
class SuggestionsRepository @Inject constructor(
    private val apiService: WingmanApiService
) {
    
    /**
     * Upload a screenshot bitmap and get AI suggestions
     * 
     * @param bitmap The screenshot bitmap
     * @param sessionId Optional session ID (generates new UUID if null)
     * @return NetworkResult containing suggestions or error
     */
    suspend fun uploadScreenshot(
        bitmap: Bitmap,
        sessionId: String? = null
    ): NetworkResult<SuggestionsResponse> = withContext(Dispatchers.IO) {
        try {
            // Compress the bitmap
            val compressedBytes = ImageCompressor.compressBitmap(bitmap)
            
            // Create multipart request body
            val requestBody = compressedBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData(
                "screenshot",
                "screenshot.jpg",
                requestBody
            )
            
            // Create session ID request body
            val sessionIdValue = sessionId ?: UUID.randomUUID().toString()
            val sessionIdBody = sessionIdValue.toRequestBody("text/plain".toMediaTypeOrNull())
            
            // Make API call
            val response = apiService.analyzeSuggestions(part, sessionIdBody)
            
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Invalid screenshot format"
                    413 -> "Screenshot file too large"
                    500 -> "AI service unavailable"
                    else -> "Failed to get suggestions (${response.code()})"
                }
                NetworkResult.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(
                message = e.message ?: "Network error occurred",
                code = null
            )
        }
    }
    
    /**
     * Upload a screenshot from file path
     * 
     * @param filePath The path to the screenshot file
     * @param sessionId Optional session ID
     * @return NetworkResult containing suggestions or error
     */
    suspend fun uploadScreenshotFromPath(
        filePath: String,
        sessionId: String? = null
    ): NetworkResult<SuggestionsResponse> = withContext(Dispatchers.IO) {
        try {
            // Compress the image file
            val compressedBytes = ImageCompressor.compressFromPath(filePath)
            
            // Create multipart request body
            val requestBody = compressedBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData(
                "screenshot",
                "screenshot.jpg",
                requestBody
            )
            
            // Create session ID request body
            val sessionIdValue = sessionId ?: UUID.randomUUID().toString()
            val sessionIdBody = sessionIdValue.toRequestBody("text/plain".toMediaTypeOrNull())
            
            // Make API call
            val response = apiService.analyzeSuggestions(part, sessionIdBody)
            
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                val errorMessage = when (response.code()) {
                    400 -> "Invalid screenshot format"
                    413 -> "Screenshot file too large"
                    500 -> "AI service unavailable"
                    else -> "Failed to get suggestions (${response.code()})"
                }
                NetworkResult.Error(errorMessage, response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(
                message = e.message ?: "Network error occurred",
                code = null
            )
        }
    }
}
