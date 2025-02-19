package com.android.smartconcierge.extensions

import android.app.Activity
import com.android.smartconcierge.service.notification.MessagingService

fun Activity.requestNotificationPermission() {
    MessagingService().requestNotificationPermission(this)
}