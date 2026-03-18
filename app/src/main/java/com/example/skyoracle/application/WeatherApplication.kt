package com.example.skyoracle.application


import android.app.Application
class WeatherApplication : Application(){

    lateinit var  container:  AppContainer
    override fun onCreate()
    {
        super.onCreate()
        container = DefaultContainer(this)
    }
}