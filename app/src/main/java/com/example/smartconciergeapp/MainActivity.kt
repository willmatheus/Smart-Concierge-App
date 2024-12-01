package com.example.smartconciergeapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.smartconciergeapp.ui.theme.screens.NotificationScreen
import com.example.smartconciergeapp.ui.theme.SmartConciergeTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartconciergeapp.service.PreferencesHelper
import com.example.smartconciergeapp.service.WebSocketService
import com.example.smartconciergeapp.ui.theme.screens.LoginScreen
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.smartconciergeapp.service.NotificationWorker
import java.util.concurrent.TimeUnit

private const val NOTIFICATION_REQUEST_CODE = 1001

class MainActivity : ComponentActivity() {
    private lateinit var webSocketService: WebSocketService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webSocketService = WebSocketService(this)
        webSocketService.start()
        setContent {
            SmartConciergeTheme {
                AppNavigation(webSocketService)
            }
        }
    }
}

@Composable
fun AppNavigation(webSocketService: WebSocketService) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val preferencesHelper = PreferencesHelper(context)
    val token = preferencesHelper.getToken()

    var visitorName by remember { mutableStateOf("") }
    var visitorCPF by remember { mutableStateOf("") }
    var hasNewRequest by remember { mutableStateOf(false) }

    // Configura o callback do WebSocket
    LaunchedEffect(webSocketService) {
        webSocketService.onNewVisitorReceived = { name, cpf ->
            visitorName = name
            visitorCPF = cpf
            hasNewRequest = true
        }
    }

    NavHost(navController = navController, startDestination = if (token != null) "home" else "login") {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("home") {
            NotificationScreen(
                webSocketService = webSocketService,
                visitorName = visitorName,
                visitorCPF = visitorCPF,
                onActionComplete = {
                    visitorName = ""
                    visitorCPF = ""
                    hasNewRequest = false
                }
            )
        }
    }
}





