package com.example.skyoracle.data

import com.example.skyoracle.model.GeoData
import com.example.skyoracle.model.WeatherData
import com.example.skyoracle.network.GeocodingApiService
import com.example.skyoracle.network.WeatherApiService

interface WeatherRepository{
    suspend fun getCordinates(city: String, limit: Int) : List<GeoData>

    suspend fun getWeatherData(lat: Double, log: Double, units: String, lang: String): WeatherData

}

class DefaultWeatherRepository(
    private val geocodingApiService: GeocodingApiService,
    private val weatherApiService: WeatherApiService,
    private val apiKey: String
): WeatherRepository
{
    override suspend fun getCordinates(city: String,
                                       limit: Int,
    ): List<GeoData> {

        return geocodingApiService.getCordinates(city = city, limit = limit, apikey = apiKey)
    }


    override suspend  fun getWeatherData(lat: Double, log: Double, units: String, lang: String): WeatherData
    =  weatherApiService.getWeatherData(lat = lat, log = log, apiKey = apiKey, units = units , lang = lang)
}