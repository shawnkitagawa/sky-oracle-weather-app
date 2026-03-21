package com.example.skyoracle.ui.theme.viewmodel

import android.R
import com.example.skyoracle.model.WeatherItem

data class resultTodayUiState(
    val cityName: String = "",
    val country: String = "",
    val weather: String = "",
    val weatherDescription: String = "",
    val tempMin: Double = 0.0,
    val tempMax : Double = 0.0,
    val pop: Double = 0.0,
    val windSpeed: Double = 0.0,
    val humidity:Int = 0,
    val hourWeather: List<HourWeather> = emptyList(),
    val OtherDayWeather: List<HourWeather> = emptyList(),
    val nextDaysWeather: List<DayWeathersSummary> = emptyList(),
    val status: Status = Status.LOADING,
)
data class DayWeathersSummary(
    val date: String,
    val tempMin: Double,
    val tempMax: Double,
    val icon: String,
    val temp: Double,
    val weatherDescription: String,
    val weather : String ,
    val pop: Double,
    val windSpeed: Double,
    val humidity: Int,
    val weatherCondition: Int,
)
data class HourWeather(
    val temp: Double = 0.0,
    val time: String = "",
    val date: String = "",
    val icon: String = "",
    val weatherConidtion: Int = 0
)

enum class Status {
    LOADING,
    SUCCESS,
    ERROR
}