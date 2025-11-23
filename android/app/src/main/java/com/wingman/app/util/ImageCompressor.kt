package com.wingman.app.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

/**
 * Utility class for compressing images before upload
 */
object ImageCompressor {
    
    private const val MAX_FILE_SIZE_BYTES = 1024 * 1024 // 1MB
    private const val INITIAL_QUALITY = 90
    private const val MIN_QUALITY = 50
    private const val QUALITY_STEP = 10
    
    /**
     * Compress a bitmap to JPEG format with target file size < 1MB
     * 
     * @param bitmap The bitmap to compress
     * @return ByteArray of compressed JPEG data
     */
    fun compressBitmap(bitmap: Bitmap): ByteArray {
        var quality = INITIAL_QUALITY
        var compressedData: ByteArray
        
        do {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            compressedData = outputStream.toByteArray()
            outputStream.close()
            
            if (compressedData.size <= MAX_FILE_SIZE_BYTES || quality <= MIN_QUALITY) {
                break
            }
            
            quality -= QUALITY_STEP
        } while (true)
        
        return compressedData
    }
    
    /**
     * Load and compress an image file
     * 
     * @param file The image file to compress
     * @return ByteArray of compressed JPEG data
     */
    fun compressFile(file: File): ByteArray {
        val bitmap = BitmapFactory.decodeStream(FileInputStream(file))
        return compressBitmap(bitmap)
    }
    
    /**
     * Load and compress an image from a file path
     * 
     * @param filePath The path to the image file
     * @return ByteArray of compressed JPEG data
     */
    fun compressFromPath(filePath: String): ByteArray {
        val bitmap = BitmapFactory.decodeFile(filePath)
        return compressBitmap(bitmap)
    }
}
