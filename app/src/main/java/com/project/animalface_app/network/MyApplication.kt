package com.project.animalface_app.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MyApplication {
    private val BASE_URL = "http://10.100.201.41:8080/" // 서버 주소를 설정
    private lateinit var apiService: INetworkService

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    fun getApiService(): INetworkService {
        if (!MyApplication::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            apiService = retrofit.create(INetworkService::class.java)
        }
        return apiService
    }
}
