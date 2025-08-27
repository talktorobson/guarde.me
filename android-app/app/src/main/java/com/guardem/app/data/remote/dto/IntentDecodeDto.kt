package com.guardem.app.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Request DTO for intent decoding endpoint
 * Matches the backend API schema exactly
 */
data class IntentDecodeRequest(
    @SerializedName("transcript_redacted")
    val transcriptRedacted: String,
    val partial: Boolean = false
)

/**
 * Response DTO for intent decoding endpoint
 * Maps to the working Gemini AI response format
 */
data class IntentDecodeResponse(
    val intent: String, // "SAVE_MEMORY", "SCHEDULE_REPLAY", "DELIVERY_CHANNEL"
    val slots: IntentSlots
)

data class IntentSlots(
    @SerializedName("content_type")
    val contentType: String, // "text", "audio", "photo", etc.
    
    @SerializedName("when_type")
    val whenType: String?, // "date", "datetime", "recurrence"
    
    @SerializedName("when_value")
    val whenValue: String?, // ISO-8601 datetime or duration like "P365D", "PT1M"
    
    val channel: String? = "push" // "in_app", "push", "email", "calendar"
)