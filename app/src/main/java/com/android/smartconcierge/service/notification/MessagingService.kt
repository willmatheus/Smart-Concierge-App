package com.android.smartconcierge.service.notification

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.android.smartconcierge.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            val title = it.title ?: "Standard"
            val body = it.body ?: "Standard body"
            sendNotification(title, body)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New token: $token")
    }

    private fun createNotificationChannel() {
        notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelName = "Standard Channel"
        val channelDescription = "Standard notification channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(CHANNEL_ID, channelName, importance)
        notificationChannel.description = channelDescription

        notificationChannel.enableVibration(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationManager.createNotificationChannel(notificationChannel)

        Log.d("Notification", "Channel created successfully")
    }

    private fun getNotificationBuilder(title: String, body: String) : NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // icon
            .setContentTitle("$title deseja visitar você!") // title
            .setContentText(body) // text body
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)// Priority
            .setContentIntent(setMainActivityContentIntent())
    }

    private fun sendNotification(title: String, body: String) {
        val builder = getNotificationBuilder(title, body)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun setMainActivityContentIntent() : PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(
            this, 1001, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun requestNotificationPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    activity, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    companion object {
        private val TAG = Companion::class.java.simpleName
        private const val CHANNEL_ID = "primary_notification_channel"
        private const val NOTIFICATION_ID = 155
    }
}