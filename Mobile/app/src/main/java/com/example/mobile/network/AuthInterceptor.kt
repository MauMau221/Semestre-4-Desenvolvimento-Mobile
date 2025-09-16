package com.example.mobile.network

import com.example.mobile.utils.PrefsHelper
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val prefsHelper: PrefsHelper) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = prefsHelper.getToken()
        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }else {
            chain.request()
        }
        return chain.proceed(request)
    }
}