package com.example.mobile.network

import com.example.mobile.network.models.LoginRequest
import com.example.mobile.network.models.LoginResponse
import com.example.mobile.network.models.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("mobile/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("user")
    suspend fun getUser(): Response<User>
}