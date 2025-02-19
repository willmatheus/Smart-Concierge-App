package com.android.smartconcierge.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val cpf: String, val apartmentNumber: String)
data class LoginResponse(val token: String, val userId: String)

interface ApiService {
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}
