package com.guardem.app.data.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.guardem.app.data.remote.api.GuardeMeApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
// import javax.inject.Inject
// import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// @Singleton  // Temporarily disabled for build fix
class NotificationRepository( // @Inject constructor(
    // private val apiService: GuardeMeApiService,
    // private val firebaseMessaging: FirebaseMessaging
) {
    // Manually instantiate Firebase Messaging for now - TODO: Re-enable Hilt
    private val firebaseMessaging by lazy { FirebaseMessaging.getInstance() }
    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun getFCMToken(): String? {
        return suspendCoroutine { continuation ->
            firebaseMessaging.token
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Timber.e(task.exception, "Fetching FCM registration token failed")
                        continuation.resume(null)
                        return@addOnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result
                    Timber.d("FCM registration token: $token")
                    continuation.resume(token)
                }
        }
    }

    fun updateFCMToken(token: String) {
        scope.launch {
            try {
                // TODO: Implement API call to register FCM token with backend
                // val request = FCMTokenRequest(token = token)
                // val response = apiService.registerFCMToken(request)
                
                Timber.d("FCM token updated (temp implementation): $token")
                // For now, just log the token - backend integration will come later
                
            } catch (e: Exception) {
                Timber.e(e, "Failed to register FCM token")
            }
        }
    }

    suspend fun subscribeToTopic(topic: String) {
        try {
            firebaseMessaging.subscribeToTopic(topic)
            Timber.d("Subscribed to FCM topic: $topic")
        } catch (e: Exception) {
            Timber.e(e, "Failed to subscribe to topic: $topic")
        }
    }

    suspend fun unsubscribeFromTopic(topic: String) {
        try {
            firebaseMessaging.unsubscribeFromTopic(topic)
            Timber.d("Unsubscribed from FCM topic: $topic")
        } catch (e: Exception) {
            Timber.e(e, "Failed to unsubscribe from topic: $topic")
        }
    }

    fun requestNotificationPermission() {
        // This will be handled by MainActivity's permission launcher
        Timber.d("Notification permission request initiated")
    }
}

data class FCMTokenRequest(
    val token: String,
    val platform: String = "android",
    val userId: String? = null
)