package com.guardem.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ScheduleCreateRequest(
    @SerializedName("user_id")
    val userId: String,
    val memory: MemoryCreateRequest,
    val schedule: ScheduleCreateRequestDetails
)

data class MemoryCreateRequest(
    @SerializedName("content_type")
    val contentType: String = "text",
    @SerializedName("content_text")
    val contentText: String,
    @SerializedName("media_path")
    val mediaPath: String? = null,
    val source: String? = null,
    val tags: List<String> = emptyList(),
    @SerializedName("privacy_mode")
    val privacyMode: String = "standard"
)

data class ScheduleCreateRequestDetails(
    @SerializedName("when_type")
    val whenType: String,
    val dtstart: String? = null,
    val rrule: String? = null,
    val timezone: String = "America/Sao_Paulo",
    val channel: String = "push"
)

data class ScheduleCreateResponse(
    val success: Boolean,
    val data: ScheduleCreateResponseData
)

data class ScheduleCreateResponseData(
    val memory: MemoryDto,
    val schedule: ScheduleDto
)

data class MemoryDto(
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("content_text")
    val contentText: String?,
    @SerializedName("content_type")
    val contentType: String,
    @SerializedName("media_path")
    val mediaPath: String?,
    val source: String?,
    val tags: List<String>,
    @SerializedName("privacy_mode")
    val privacyMode: String,
    @SerializedName("created_at")
    val createdAt: String
)

data class ScheduleDto(
    val id: String,
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("memory_id")
    val memoryId: String,
    @SerializedName("when_type")
    val whenType: String,
    val dtstart: String?,
    val rrule: String?,
    val timezone: String,
    val status: String,
    @SerializedName("next_run_at")
    val nextRunAt: String?,
    @SerializedName("created_at")
    val createdAt: String
)

data class DeliveryRunResponse(
    val processed: Int,
    val successful: Int,
    val failed: Int
)

data class MemoriesResponse(
    val success: Boolean,
    val data: List<MemoryDto>,
    val count: Int
)