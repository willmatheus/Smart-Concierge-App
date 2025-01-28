package com.example.smartconciergeapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import com.example.smartconciergeapp.ui.AppNavigation
import com.example.smartconciergeapp.ui.SmartConciergeTheme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()
        // Configurar a interface do aplicativo
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Falha ao obter token", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM", "Token do dispositivo: $token")
            setContent {
                SmartConciergeTheme {
                    AppNavigation()
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        // Solicitar permissão para notificações
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}




