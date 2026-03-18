package com.example.skyoracle.ui.theme.viewmodel

import com.example.skyoracle.model.CityInfo

data class resultUiState(
    val cityInfo: CityUi = CityUi(),
    val forecast: List<ForeCastUi> = emptyList(),

    )
data class CityUi(
    val cityName: String = "",
    val country: String = "",
    val timezone: Long = 0
)
data class ForeCastUi(
    val time: Long = 0 ,
    val temp: Double = 0.0,
    val feelsLike: Double = 0.0,
    val tempMin: Double = 0.0,
    val tempMax: Double = 0.0,
    val humidity: Int = 0 ,
    val weather: String = "",
    val description: String = "",
    val icon: String,
    val probability: Double = 0.0,
    val partOfDay: String = ""
)