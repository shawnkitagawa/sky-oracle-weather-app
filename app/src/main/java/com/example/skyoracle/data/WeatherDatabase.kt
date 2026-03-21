package com.example.skyoracle.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.skyoracle.model.Weather


@Database(entities = [Weather::class], version = 2, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase()
{

    abstract fun weatherDao(): WeatherDao
    companion object{
        @Volatile
        private var Instance: WeatherDatabase?= null

        fun getDatabase(context: Context) : WeatherDatabase {

            return Instance ?: synchronized(this)
            {
                Room.databaseBuilder(context.applicationContext, WeatherDatabase::class.java, "weather_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}