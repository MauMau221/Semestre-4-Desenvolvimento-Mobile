package com.example.mobile.data

import android.content.Context
import com.example.mobile.data.api.ApiService
import com.example.mobile.data.api.dtos.AuthInterceptor
import com.example.mobile.utils.PrefsHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = Constants.BASE_URL

    fun getInstance(context: Context): ApiService {
        val prefsHelper = PrefsHelper(context)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(prefsHelper)) // add token
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
}