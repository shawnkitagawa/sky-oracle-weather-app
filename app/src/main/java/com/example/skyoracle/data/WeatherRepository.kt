package com.example.skyoracle.data

import android.util.Log
import com.example.skyoracle.model.GeoData
import com.example.skyoracle.model.Weather
import com.example.skyoracle.model.WeatherData
import com.example.skyoracle.network.GeocodingApiService
import com.example.skyoracle.network.WeatherApiService
import kotlinx.coroutines.flow.Flow

interface WeatherRepository{
    suspend fun getCordinates(city: String, limit: Int) : List<GeoData>

    suspend fun getWeatherData(lat: Double, lon: Double, units: String, lang: String): List<Weather>

}

class DefaultWeatherRepository(
    private val geocodingApiService: GeocodingApiService,
    private val weatherApiService: WeatherApiService,
    private val apiKey: String,
    private val weatherDao: WeatherDao
): WeatherRepository
{
    override suspend fun getCordinates(city: String,
                                       limit: Int,
    ): List<GeoData> {

        return geocodingApiService.getCordinates(city = city, limit = limit, apikey = apiKey)
    }

    fun WeatherData.toDomain(lat: Double, lon: Double): List<Weather> {
        return weatherList.map { item ->
            Weather(
                timeStamp = item.time,
                cityName = city.cityName,
                country = city.country,
                dateText = item.dtTxt,
                temp = item.main.temp,
                tempMin = item.main.tempMin,
                tempMax = item.main.tempMax,
                pop = item.probability,
                windSpeed = item.wind.speed,
                humidity = item.main.humidity,
                icon = item.weather.firstOrNull()?.icon ?: "",
                weather = item.weather.firstOrNull()?.weather ?: "",
                weatherDescription = item.weather.firstOrNull()?.description ?: "",
                lat = lat,
                lon = lon,
                dtTxt = item.dtTxt,
                weatherCondition = item.weather.firstOrNull()?.id ?: 0,
            )
        }
    }
    fun isExpired(cache: List<Weather>): Boolean{
        var now = System.currentTimeMillis()
        val lastFetched = cache.first().timeStamp
       return  now - lastFetched > 1 * 69 * 60 * 1000 // one hour

    }
    override suspend fun getWeatherData(lat: Double, lon: Double, units: String, lang: String):List<Weather> {
        val cache = weatherDao.getWeatherData(lat, lon)
        if (cache.isEmpty() || isExpired(cache)) {
            val response = weatherApiService
                .getWeatherData(lat = lat, lon = lon, apiKey = apiKey, units = units, lang = lang)
                .toDomain(lat, lon)
            Log.d("Cache Deleted", "Cashe was deleted ${cache}!!!")
            weatherDao.delete(lat, lon)
            weatherDao.insert(weatherDatas = response)
            return response

        } else {
            Log.d("Cashe usesd", "Cashe is ${cache}")
            return cache
        }
    }

}