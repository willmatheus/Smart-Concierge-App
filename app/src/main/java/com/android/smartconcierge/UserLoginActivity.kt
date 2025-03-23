package com.android.smartconcierge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.android.smartconcierge.ui.SmartConciergeTheme
import com.android.smartconcierge.ui.screens.LoginScreen

class UserLoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartConciergeTheme {
                LoginScreen(this)
            }
        }
    }
}