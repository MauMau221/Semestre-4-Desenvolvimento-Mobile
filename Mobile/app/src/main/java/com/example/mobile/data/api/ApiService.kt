package com.example.mobile.data.api

import com.example.mobile.data.api.dtos.ClienteDTO
import com.example.mobile.data.models.clientes.Clientes
import com.example.mobile.data.models.users.LoginRequest
import com.example.mobile.data.models.users.LoginResponse
import com.example.mobile.data.models.users.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("clientes")
    suspend fun getClientes(): Response<ClienteDTO>

    @GET("user")
    suspend fun getUser(): Response<User>
}