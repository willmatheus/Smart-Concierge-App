package com.android.smartconcierge.service.notification

import android.app.NotificationManager
import android.util.Log
import com.android.smartconcierge.extensions.createNotificationChannel
import com.android.smartconcierge.extensions.getMainActivityContentIntent
import com.android.smartconcierge.extensions.getNotificationBuilder
import com.android.smartconcierge.extensions.isUserLoggedIn
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null && isUserLoggedIn()) {
            val title = message.notification!!.title ?: "Standard"
            val body = message.notification!!.body ?: "Standard body"

            val notification = getNotificationBuilder(
                title, body, getMainActivityContentIntent()
            ).build()

            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = createNotificationChannel()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New token: $token")
    }

    companion object {
        private val TAG = Companion::class.java.simpleName
        const val CHANNEL_ID = "primary_notification_channel"
        const val NOTIFICATION_ID = 155
    }
}