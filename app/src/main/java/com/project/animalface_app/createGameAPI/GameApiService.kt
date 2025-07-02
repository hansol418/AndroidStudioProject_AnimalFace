package com.project.animalface_app.createGameAPI

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GameApiService {
    @POST("api/createGame/create")
    fun createGame(@Body createGame: CreateGame): Call<CreateGame>
}
