package com.example.skyoracle.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import com.example.skyoracle.model.Weather
import kotlinx.coroutines.flow.Flow


@Dao
interface WeatherDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherDatas: List<Weather>)


    @Query("""
        DELETE FROM weather
        WHERE lat  = :lat AND lon = :lon
    """)
    suspend fun delete(lat: Double, lon: Double)

    @Query("""
        SELECT * FROM weather
        WHERE lat = :lat AND lon = :lon
    """)
    suspend fun getWeatherData(lat:Double, lon: Double): List<Weather>
}