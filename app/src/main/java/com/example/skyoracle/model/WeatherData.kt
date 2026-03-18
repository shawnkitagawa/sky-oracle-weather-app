package com.example.skyoracle.model

import android.opengl.Visibility
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherData(
    @SerialName("cod")
    val code: String,

    @SerialName("cnt")
    val forecastCount: Int,

    @SerialName("list")
    val weatherList: List<WeatherItem>,

    val city: CityInfo
)

@Serializable
data class WeatherItem(
    @SerialName("dt")
    val time: Long,

    val main: MainItem,
    val weather: List<WeatherInfo>,
    val clouds: CloudInfo,
    val wind: WindInfo,
    val visibility: Int,

    @SerialName("pop")
    val probability: Double,

    val rain: RainInfo? = null,
    val snow: SnowInfo? = null,
    val sys: SysInfo,

    @SerialName("dt_txt")
    val dtTxt: String
)

@Serializable
data class CityInfo(
    @SerialName("name")
    val cityName: String,

    @SerialName("country")
    val country: String,

    val timezone: Long
)

@Serializable
data class SysInfo(
    @SerialName("pod")
    val partOfDay: String
)

@Serializable
data class SnowInfo(
    @SerialName("3h")
    val snowVolume: Double
)

@Serializable
data class RainInfo(
    @SerialName("3h")
    val rainVolume: Double
)

@Serializable
data class WindInfo(
    val speed: Double,

    @SerialName("deg")
    val windDirection: Int
)

@Serializable
data class CloudInfo(
    @SerialName("all")
    val cloudCoverage: Int
)

@Serializable
data class WeatherInfo(
    val id: Int,

    @SerialName("main")
    val weather: String,

    val description: String,
    val icon: String
)

@Serializable
data class MainItem(
    val temp: Double,

    @SerialName("feels_like")
    val feelsLike: Double,

    @SerialName("temp_min")
    val tempMin: Double,

    @SerialName("temp_max")
    val tempMax: Double,

    val pressure: Int,
    val humidity: Int
)