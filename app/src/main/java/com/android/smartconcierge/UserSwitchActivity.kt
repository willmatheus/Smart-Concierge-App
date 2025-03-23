package com.android.smartconcierge

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.android.smartconcierge.extensions.isUserLoggedIn
import com.android.smartconcierge.extensions.requestNotificationPermission

class UserSwitchActivity : ComponentActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { checkUserToken() }
        requestNotificationPermission(permissionLauncher)
    }

    private fun checkUserToken() {
        val intent = if (isUserLoggedIn()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, UserLoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}