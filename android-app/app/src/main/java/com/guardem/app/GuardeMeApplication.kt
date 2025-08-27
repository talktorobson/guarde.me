package com.guardem.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class GuardeMeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Create notification channels
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            
            // Memory delivery channel
            val memoryChannel = NotificationChannel(
                MEMORY_DELIVERY_CHANNEL_ID,
                "Entregas de Memórias",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificações quando suas memórias são entregues"
                enableVibration(true)
                setShowBadge(true)
            }
            
            // General app channel
            val generalChannel = NotificationChannel(
                GENERAL_CHANNEL_ID,
                "Geral",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificações gerais do app"
            }
            
            notificationManager.createNotificationChannels(
                listOf(memoryChannel, generalChannel)
            )
        }
    }

    companion object {
        const val MEMORY_DELIVERY_CHANNEL_ID = "memory_delivery"
        const val GENERAL_CHANNEL_ID = "general"
    }
}