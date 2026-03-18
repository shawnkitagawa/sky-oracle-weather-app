package com.example.skyoracle.network

import com.example.skyoracle.model.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET(value = "data/2.5/forecast")
    suspend fun getWeatherData
    (
        @Query(value = "lat") lat: Double,
        @Query(value = "log") log: Double,
        @Query(value = "appid") apiKey: String,
        @Query(value = "units") units: String,
        @Query(value = "lang") lang: String

    ):WeatherData
}