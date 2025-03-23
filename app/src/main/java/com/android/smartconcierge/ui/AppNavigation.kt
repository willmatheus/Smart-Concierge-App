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
import com.android.smartconcierge.ui.screens.HomeScreen
import com.android.smartconcierge.ui.screens.LoginScreen
import com.android.smartconcierge.enums.Screens.*
import com.android.smartconcierge.service.AppPreferences
import com.android.smartconcierge.service.AppPreferences.PreferencesKeys.USER_TOKEN

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val appPreferences = AppPreferences(context)
    val token = appPreferences.getPrefString(USER_TOKEN)

    var visitorName by remember { mutableStateOf("") }
    var visitorCPF by remember { mutableStateOf("") }
    var hasNewRequest by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = if (token != null) MAIN_SCREEN.name else LOGIN_SCREEN.name
    ) {
        composable(LOGIN_SCREEN.name) {
            LoginScreen(navController = navController)
        }
        composable(MAIN_SCREEN.name) {
            HomeScreen(
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