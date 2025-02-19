package com.android.smartconcierge.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.smartconcierge.service.PreferencesHelper
import com.android.smartconcierge.ui.screens.LoginScreen
import com.android.smartconcierge.ui.screens.NotificationScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val preferencesHelper = PreferencesHelper(context)
    val token = preferencesHelper.getToken()

    var visitorName by remember { mutableStateOf("") }
    var visitorCPF by remember { mutableStateOf("") }
    var hasNewRequest by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = if (token != null) "home" else "login"
    ) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("home") {
            NotificationScreen(
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