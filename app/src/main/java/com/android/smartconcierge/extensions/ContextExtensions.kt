package com.android.smartconcierge.extensions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.smartconcierge.MainActivity
import com.android.smartconcierge.service.notification.MessagingService.Companion.CHANNEL_ID
import com.android.smartconcierge.service.notification.MessagingService.Companion.NOTIFICATION_ID

fun Context.createNotificationChannel() : NotificationManager {
    val notificationManager =
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

    return notificationManager
}

fun Context.getNotificationBuilder(title: String, body: String, intent: PendingIntent) : NotificationCompat.Builder {
    return NotificationCompat.Builder(applicationContext, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_dialog_info) // icon
        .setContentTitle(title) // title
        .setContentText(body) // text body
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setDefaults(NotificationCompat.DEFAULT_ALL)// Priority
        .setContentIntent(intent)
}

fun Context.sendNotification(notificationBuilder: NotificationCompat.Builder, notificationManager: NotificationManager) {
    notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
}

fun Context.getMainActivityContentIntent() : PendingIntent {
    val intent = Intent(this, MainActivity::class.java)
    return PendingIntent.getActivity(
        this, 1001, intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
}