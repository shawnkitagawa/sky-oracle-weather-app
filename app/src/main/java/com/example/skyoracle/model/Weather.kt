package com.example.skyoracle.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "weather")
data class Weather(
    @PrimaryKey
    val timeStamp: Long,
    val cityName: String,
    val country: String,
    val dateText: String,
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pop: Double,
    val windSpeed: Double,
    val humidity: Int,
    val icon: String,
    val weather: String,
    val weatherDescription: String,
    val lat: Double,
    val lon: Double,
    val dtTxt: String,
    val weatherCondition: Int,
)
