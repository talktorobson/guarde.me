package com.guardem.app.data.remote.api

import com.guardem.app.data.remote.dto.DeliveryRunResponse
import com.guardem.app.data.remote.dto.IntentDecodeRequest
import com.guardem.app.data.remote.dto.IntentDecodeResponse
import com.guardem.app.data.remote.dto.ScheduleCreateRequest
import com.guardem.app.data.remote.dto.ScheduleCreateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API service for Guarde.me backend endpoints
 * Connects to the operational Next.js backend with Gemini AI integration
 */
interface GuardeMeApiService {

    /**
     * Decode voice transcript into structured intent using Gemini AI
     * Backend endpoint: POST /api/intent/decode
     */
    @POST("api/intent/decode")
    suspend fun decodeIntent(
        @Body request: IntentDecodeRequest
    ): Response<IntentDecodeResponse>

    /**
     * Create a memory with scheduled delivery
     * Backend endpoint: POST /api/schedule/create
     */
    @POST("api/schedule/create")
    suspend fun createSchedule(
        @Body request: ScheduleCreateRequest
    ): Response<ScheduleCreateResponse>

    /**
     * Trigger delivery run for pending memories
     * Backend endpoint: POST /api/deliver/run
     */
    @POST("api/deliver/run")
    suspend fun runDelivery(): Response<DeliveryRunResponse>
}