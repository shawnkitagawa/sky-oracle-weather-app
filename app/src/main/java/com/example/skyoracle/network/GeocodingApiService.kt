package com.example.skyoracle.network

import com.example.skyoracle.model.GeoData
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingApiService {
    @GET("geo/1.0/direct")
    suspend fun getCordinates(
        @Query ("q") city: String,
        @Query ("limit") limit: Int,
        @Query("appid") apikey: String
    ) : List<GeoData>
}

