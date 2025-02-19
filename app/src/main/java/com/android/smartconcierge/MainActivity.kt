package com.android.smartconcierge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.util.Log
import com.android.smartconcierge.extensions.requestNotificationPermission
import com.android.smartconcierge.ui.AppNavigation
import com.android.smartconcierge.ui.SmartConciergeTheme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fail to get token", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM", "Device token: $token")
        }
        setContent {
            SmartConciergeTheme {
                AppNavigation()
            }
        }
    }
}




