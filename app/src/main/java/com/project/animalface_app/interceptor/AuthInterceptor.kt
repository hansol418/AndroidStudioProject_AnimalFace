package com.project.animalface_app.interceptor

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = sharedPreferences.getString("jwt_token", null)

        val newRequest = if (token != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(newRequest)
    }
}