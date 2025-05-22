package com.vaishnavi.tryretro.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("current.json")
    suspend fun getWeather(
        @Query("key") apikey: String,
        @Query("q") city: String
    ): Response<WeatherModel>
}
