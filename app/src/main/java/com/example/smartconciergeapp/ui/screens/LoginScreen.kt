package com.example.smartconciergeapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.smartconciergeapp.data.RetrofitClient
import com.example.smartconciergeapp.service.LoginRequest
import com.example.smartconciergeapp.service.LoginResponse
import com.example.smartconciergeapp.service.PreferencesHelper
import retrofit2.Call


@Composable
fun LoginScreen(navController: NavHostController) {
    val context = LocalContext.current
    var cpf by remember { mutableStateOf("") }
    var apartmentNumber by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val preferencesHelper = PreferencesHelper(context)

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
                                preferencesHelper.saveToken(token)
                                navController.navigate("home")
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