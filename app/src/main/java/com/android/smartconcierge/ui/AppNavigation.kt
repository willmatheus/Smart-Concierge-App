package com.android.smartconcierge.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.smartconcierge.ui.screens.HomeScreen
import com.android.smartconcierge.enums.Screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var visitorName by remember { mutableStateOf("") }
    var visitorCPF by remember { mutableStateOf("") }
    var hasNewRequest by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = MAIN_SCREEN.name
    ) {
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