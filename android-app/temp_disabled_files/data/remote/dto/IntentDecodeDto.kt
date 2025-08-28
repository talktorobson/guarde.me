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
    val contentType: String?, // "text", "voice", "photo", "screenshot", "image_link", "selection"
    
    @SerializedName("content_source")
    val contentSource: String?, // "selected_text", "camera", "gallery", "clipboard", "url", "screen_share"
    
    @SerializedName("topic_tags")
    val topicTags: List<String>?, // Array of topic tags
    
    @SerializedName("when_type")
    val whenType: String?, // "date", "datetime", "recurrence"
    
    @SerializedName("when_value")
    val whenValue: String?, // ISO-8601 datetime or RRULE
    
    val channel: String? // "in_app", "push", "email", "calendar"
)