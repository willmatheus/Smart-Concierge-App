package com.android.smartconcierge.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.smartconcierge.MainActivity
import com.android.smartconcierge.data.RetrofitClient
import com.android.smartconcierge.service.AppPreferences
import com.android.smartconcierge.service.AppPreferences.PreferencesKeys.USER_TOKEN
import com.android.smartconcierge.service.LoginRequest
import com.android.smartconcierge.service.LoginResponse
import retrofit2.Call

@Composable
fun LoginScreen(activity: Activity) {
    var cpf by remember { mutableStateOf("") }
    var apartmentNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val appPreferences = AppPreferences(activity)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // CPF Field
        OutlinedTextField(
            value = cpf,
            onValueChange = { if (it.length <= 11 && it.all { char -> char.isDigit() }) cpf = it },
            label = { Text("CPF") },
            placeholder = { Text("Apenas números") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Apartment Number Field
        OutlinedTextField(
            value = apartmentNumber,
            onValueChange = {
                if (it.length <= 4 && it.all { char -> char.isDigit() }) apartmentNumber = it
            },
            label = { Text("Número do Apartamento") },
            placeholder = { Text("Ex: 101") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        // Login Button
        Button(
            onClick = {
                val api = RetrofitClient.instance
                val loginRequest = LoginRequest(cpf, apartmentNumber)

                api.login(loginRequest).enqueue(object : retrofit2.Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: retrofit2.Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            val token = response.body()?.token
                            if (token != null) {
                                appPreferences.savePref(USER_TOKEN, token)
                                val intent = Intent(activity, MainActivity::class.java)
                                activity.startActivity(intent)
                                activity.finish()
                            } else {
                                errorMessage = "Erro ao salvar token."
                            }
                        } else {
                            errorMessage = "Login inválido. Tente novamente."
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        errorMessage = "Erro de conexão: ${t.message}"
                    }
                })
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Entrar")
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}