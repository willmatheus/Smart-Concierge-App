package com.example.smartconciergeapp.service

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    // Função para salvar o token
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    // Função para obter o token
    fun getToken(): String? {
        return sharedPreferences.getString("token", null)  // Retorna null se o token não existir
    }

    // Função para remover o token (no logout, por exemplo)
    fun removeToken() {
        sharedPreferences.edit().remove("token").apply()
    }
}

