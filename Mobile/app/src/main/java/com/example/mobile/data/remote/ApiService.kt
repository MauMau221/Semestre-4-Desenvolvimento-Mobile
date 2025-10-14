package com.example.mobile.data.remote

import ClienteResponse
import com.example.mobile.data.models.User
import com.example.mobile.data.remote.dtos.LoginRequest
import com.example.mobile.data.remote.dtos.LoginResponse
import com.example.mobile.data.remote.dtos.NewClienteRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>


    @POST("me")
    suspend fun checkToken(@Header("Authorization") token: String): Response<User>

    @GET("clientes")
    suspend fun getClientes(): Response<ClienteResponse>

    @POST("clientes")
    suspend fun createCliente(@Body newClient: NewClienteRequest): Response<Void>

}