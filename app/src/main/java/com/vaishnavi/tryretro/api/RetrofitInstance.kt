package com.vaishnavi.tryretro.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val baseurl = "https://api.weatherapi.com/v1/"

object RetrofitInstance {
    private fun getInstance(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val weatherApi : WeatherApi = getInstance().create(WeatherApi::class.java)
}