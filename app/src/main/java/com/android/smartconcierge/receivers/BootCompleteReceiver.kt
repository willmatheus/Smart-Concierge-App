package com.android.smartconcierge.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.smartconcierge.R
import com.android.smartconcierge.extensions.createNotificationChannel
import com.android.smartconcierge.extensions.getNotificationBuilder
import com.android.smartconcierge.extensions.getMainActivityContentIntent
import com.android.smartconcierge.extensions.isUserLoggedIn
import com.android.smartconcierge.service.notification.PushNotificationService.Companion.NOTIFICATION_ID

class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val appContext = context.applicationContext
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED && appContext.isUserLoggedIn()) {
            val notificationManager = appContext.createNotificationChannel()

            val notification = appContext.let {
                it.getNotificationBuilder(
                    it.getString(R.string.boot_notification_title),
                    it.getString(R.string.boot_notification_body),
                    it.getMainActivityContentIntent()
                ).build()
            }

            Log.d("BootCompleteReceiver", "BOOT BROADCAST RECEIVED!")
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
}