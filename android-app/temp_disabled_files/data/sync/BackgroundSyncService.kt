package com.guardem.app.data.sync

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.*
import com.guardem.app.data.notification.NotificationRepository
import com.guardem.app.data.remote.api.GuardeMeApiService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class BackgroundSyncService : Service() {

    @Inject
    lateinit var apiService: GuardeMeApiService
    
    @Inject
    lateinit var notificationRepository: NotificationRepository

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        schedulePendingMemoriesSync()
        Timber.d("BackgroundSyncService created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SYNC_PENDING_MEMORIES -> {
                syncPendingMemories()
            }
            ACTION_SYNC_FCM_TOKEN -> {
                syncFCMToken()
            }
            ACTION_CLEANUP_OLD_DATA -> {
                cleanupOldData()
            }
            else -> {
                performFullSync()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        Timber.d("BackgroundSyncService destroyed")
    }

    private fun schedulePendingMemoriesSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<MemorySyncWorker>(
            15, TimeUnit.MINUTES,
            5, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "memory_sync",
                ExistingPeriodicWorkPolicy.KEEP,
                syncWorkRequest
            )

        Timber.d("Scheduled periodic memory sync")
    }

    private fun syncPendingMemories() {
        scope.launch {
            try {
                Timber.d("Syncing pending memories...")
                
                // Trigger delivery run on backend
                val response = apiService.runDelivery()
                if (response.isSuccessful) {
                    val result = response.body()
                    Timber.d("Delivery run completed: $result")
                } else {
                    Timber.w("Delivery run failed: ${response.code()}")
                }
                
            } catch (e: Exception) {
                Timber.e(e, "Failed to sync pending memories")
            }
        }
    }

    private fun syncFCMToken() {
        scope.launch {
            try {
                val token = notificationRepository.getFCMToken()
                if (token != null) {
                    notificationRepository.updateFCMToken(token)
                    Timber.d("FCM token synced successfully")
                } else {
                    Timber.w("Failed to get FCM token")
                }
            } catch (e: Exception) {
                Timber.e(e, "Failed to sync FCM token")
            }
        }
    }

    private fun cleanupOldData() {
        scope.launch {
            try {
                Timber.d("Cleaning up old data...")
                
                // Clear app cache
                applicationContext.cacheDir.listFiles()?.forEach { file ->
                    if (file.lastModified() < System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7)) {
                        file.delete()
                        Timber.d("Deleted old cache file: ${file.name}")
                    }
                }
                
                // TODO: Cleanup old local database entries if needed
                
                Timber.d("Data cleanup completed")
                
            } catch (e: Exception) {
                Timber.e(e, "Failed to cleanup old data")
            }
        }
    }

    private fun performFullSync() {
        scope.launch {
            try {
                Timber.d("Performing full sync...")
                
                // Sync FCM token
                syncFCMToken()
                
                // Sync pending memories
                syncPendingMemories()
                
                // Cleanup old data
                cleanupOldData()
                
                Timber.d("Full sync completed")
                
            } catch (e: Exception) {
                Timber.e(e, "Failed to perform full sync")
            }
        }
    }

    companion object {
        const val ACTION_SYNC_PENDING_MEMORIES = "com.guardem.app.SYNC_PENDING_MEMORIES"
        const val ACTION_SYNC_FCM_TOKEN = "com.guardem.app.SYNC_FCM_TOKEN"
        const val ACTION_CLEANUP_OLD_DATA = "com.guardem.app.CLEANUP_OLD_DATA"
    }
}

class MemorySyncWorker(
    context: android.content.Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Timber.d("MemorySyncWorker started")
            
            // Start background sync service
            val intent = Intent(applicationContext, BackgroundSyncService::class.java).apply {
                action = BackgroundSyncService.ACTION_SYNC_PENDING_MEMORIES
            }
            applicationContext.startService(intent)
            
            Timber.d("MemorySyncWorker completed")
            Result.success()
            
        } catch (e: Exception) {
            Timber.e(e, "MemorySyncWorker failed")
            Result.retry()
        }
    }
}