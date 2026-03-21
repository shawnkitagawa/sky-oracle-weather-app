package com.example.skyoracle.application

import android.content.Context
import com.example.skyoracle.BuildConfig
import com.example.skyoracle.data.WeatherRepository
import com.example.skyoracle.data.DefaultWeatherRepository
import com.example.skyoracle.data.WeatherDatabase
import com.example.skyoracle.network.GeocodingApiService
import com.example.skyoracle.network.WeatherApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer{
    val weatherRepositiory: WeatherRepository
}

class DefaultContainer( private val context: Context

):AppContainer
{
    private val BASE_URL = "https://api.openweathermap.org/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitServiceGeo: GeocodingApiService by lazy {
        retrofit.create(GeocodingApiService::class.java)
    }
    private val retrofitServiceWeather: WeatherApiService by lazy {
        retrofit.create(WeatherApiService::class.java)
    }

    override val weatherRepositiory: WeatherRepository by lazy {
        DefaultWeatherRepository( geocodingApiService = retrofitServiceGeo,
            weatherApiService = retrofitServiceWeather,
            apiKey = BuildConfig.WEATHER_API_KEY,
            weatherDao = WeatherDatabase.getDatabase(context).weatherDao())
    }
}


