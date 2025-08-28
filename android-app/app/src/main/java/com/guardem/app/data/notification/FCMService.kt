package com.guardem.app.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.guardem.app.MainActivity
import com.guardem.app.R
import com.guardem.app.ui.theme.GuardeMePrimary
// import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
// import javax.inject.Inject

// @AndroidEntryPoint  // Temporarily disabled for build fix
class FCMService : FirebaseMessagingService() {

    // @Inject  // Temporarily disabled for build fix
    // lateinit var notificationRepository: NotificationRepository
    
    private val notificationRepository by lazy {
        // Manually instantiate for now - TODO: Re-enable Hilt
        NotificationRepository()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        Timber.d("FCM message received from: ${remoteMessage.from}")
        
        // Check if message contains a data payload
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("Message data payload: ${remoteMessage.data}")
            handleDataMessage(remoteMessage.data)
        }

        // Check if message contains a notification payload
        remoteMessage.notification?.let { notification ->
            Timber.d("Message notification body: ${notification.body}")
            showNotification(
                title = notification.title ?: "Guarde.me",
                body = notification.body ?: "Nova memória chegou!",
                data = remoteMessage.data
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("Refreshed FCM token: $token")
        
        // Send the new token to the backend
        notificationRepository.updateFCMToken(token)
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val type = data["type"]
        val memoryId = data["memory_id"]
        val content = data["content"]
        
        when (type) {
            "memory_delivery" -> {
                showMemoryDeliveryNotification(
                    memoryId = memoryId ?: "",
                    content = content ?: "Sua memória chegou!",
                    data = data
                )
            }
            "reminder" -> {
                showReminderNotification(
                    content = content ?: "Você tem um lembrete!",
                    data = data
                )
            }
            else -> {
                showGenericNotification(
                    content = content ?: "Nova notificação do Guarde.me",
                    data = data
                )
            }
        }
    }

    private fun showMemoryDeliveryNotification(
        memoryId: String,
        content: String,
        data: Map<String, String>
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("memory_id", memoryId)
            putExtra("open_memories", true)
        }
        
        showNotification(
            title = "🧠 Memória Entregue",
            body = content,
            data = data,
            intent = intent,
            channelId = NotificationChannels.MEMORY_DELIVERY
        )
    }

    private fun showReminderNotification(
        content: String,
        data: Map<String, String>
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        showNotification(
            title = "⏰ Lembrete",
            body = content,
            data = data,
            intent = intent,
            channelId = NotificationChannels.REMINDERS
        )
    }

    private fun showGenericNotification(
        content: String,
        data: Map<String, String>
    ) {
        showNotification(
            title = "Guarde.me",
            body = content,
            data = data,
            channelId = NotificationChannels.GENERAL
        )
    }

    private fun showNotification(
        title: String,
        body: String,
        data: Map<String, String>,
        intent: Intent? = null,
        channelId: String = NotificationChannels.GENERAL
    ) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        // Create notification channel if needed (Android O+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager, channelId)
        }

        val pendingIntent = if (intent != null) {
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setColor(GuardeMePrimary.hashCode())

        // Add action buttons for memory delivery notifications
        if (channelId == NotificationChannels.MEMORY_DELIVERY) {
            val viewIntent = Intent(this, MainActivity::class.java).apply {
                putExtra("open_memories", true)
            }
            val viewPendingIntent = PendingIntent.getActivity(
                this,
                1,
                viewIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            
            notificationBuilder.addAction(
                R.drawable.ic_notification,
                "Ver Memórias",
                viewPendingIntent
            )
        }

        notificationManager.notify(
            data["notification_id"]?.toIntOrNull() ?: System.currentTimeMillis().toInt(),
            notificationBuilder.build()
        )
    }

    private fun createNotificationChannel(
        notificationManager: NotificationManager,
        channelId: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = when (channelId) {
                NotificationChannels.MEMORY_DELIVERY -> NotificationChannel(
                    channelId,
                    "Entrega de Memórias",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notificações quando suas memórias são entregues"
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 300, 300, 300)
                }
                
                NotificationChannels.REMINDERS -> NotificationChannel(
                    channelId,
                    "Lembretes",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Lembretes e alertas importantes"
                    enableVibration(true)
                }
                
                else -> NotificationChannel(
                    channelId,
                    "Geral",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Notificações gerais do app"
                }
            }
            
            notificationManager.createNotificationChannel(channel)
        }
    }
}

object NotificationChannels {
    const val MEMORY_DELIVERY = "memory_delivery"
    const val REMINDERS = "reminders"
    const val GENERAL = "general"
}