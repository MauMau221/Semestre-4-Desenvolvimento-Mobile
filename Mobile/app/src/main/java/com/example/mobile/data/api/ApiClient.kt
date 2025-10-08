package com.example.mobile.data.api

import com.example.mobile.data.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object ApiClient {
    private const val BASE_URL = Constants.BASE_URL
    private val client by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    val retrofit: Retrofit by lazy { //by lazy indica que o retrofit só será iniciado quando for usado a primeira vez
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory
            .create())
            .build()
    }

}