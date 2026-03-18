package com.example.skyoracle.model

import kotlinx.serialization.Serializable

@Serializable
data class GeoData(
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val state: String? = null
)
