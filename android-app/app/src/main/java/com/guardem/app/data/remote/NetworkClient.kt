package com.guardem.app.data.remote

import com.google.gson.GsonBuilder
import com.guardem.app.BuildConfig
import com.guardem.app.data.remote.api.GuardeMeApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Simple network client for Guarde.me API
 * Provides configured API service instances without dependency injection
 */
object NetworkClient {
    
    private val gson by lazy {
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
    }
    
    private val okHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)

        // Add logging interceptor for debug builds
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        builder.build()
    }
    
    private val backendRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_URL + "/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    val apiService: GuardeMeApiService by lazy {
        backendRetrofit.create(GuardeMeApiService::class.java)
    }
}