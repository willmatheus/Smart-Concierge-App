package com.android.smartconcierge.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.smartconcierge.R
import com.android.smartconcierge.extensions.createNotificationChannel
import com.android.smartconcierge.extensions.getNotificationBuilder
import com.android.smartconcierge.extensions.sendNotification
import com.android.smartconcierge.extensions.getMainActivityContentIntent

class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val appContext = context.applicationContext
            val notificationManager = appContext.createNotificationChannel()

            val notificationBuilder = appContext.let {
                it.getNotificationBuilder(
                    it.getString(R.string.boot_notification_title),
                    it.getString(R.string.boot_notification_body),
                    it.getMainActivityContentIntent()
                )
            }

            Log.d("BootCompleteReceiver", "BOOT BROADCAST RECEIVED!")
            appContext.sendNotification(notificationBuilder, notificationManager)
        }
    }
}