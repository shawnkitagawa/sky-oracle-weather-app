package com.example.skyoracle

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.skyoracle.ui.theme.navigation.WeatherNavHost

@Composable

fun WeatherApp(
    navController: NavHostController = rememberNavController()
)
{
    Scaffold()
    {
        it ->
        WeatherNavHost(navController = navController)
    }

}
