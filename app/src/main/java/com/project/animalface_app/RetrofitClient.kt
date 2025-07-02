package com.project.animalface_app

import android.os.Build
import androidx.annotation.RequiresApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
object RetrofitClient {
    private const val BASE_URL = "http://10.100.201.41:8080/"

    val instance: Retrofit by lazy {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateDeserializer())
            .create()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}
