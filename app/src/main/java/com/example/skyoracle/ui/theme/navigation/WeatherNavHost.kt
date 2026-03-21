package com.example.skyoracle.ui.theme.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.skyoracle.ui.theme.screens.result.Next5DaysScreen
import com.example.skyoracle.ui.theme.screens.result.TodayScreen
import com.example.skyoracle.ui.theme.screens.result.TodayScreen
import com.example.skyoracle.ui.theme.screens.search.SearchScreen

@Composable
fun WeatherNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController, startDestination = "search", modifier = modifier
    )
    {
        composable (route = "search")
        {

            SearchScreen(oncitySelected = {lat, lon, name -> navController.navigate(route = "result/$lat/$lon/$name") })
        }
        composable(route = "result/{lat}/{lon}/{name}")
        { backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull()
            val lon = backStackEntry.arguments?.getString("lon")?.toDoubleOrNull()
            val cityName = backStackEntry.arguments?.getString("name")
            if (lat != null && lon != null && cityName != null)
            {
                TodayScreen(lat = lat,lon = lon, cityName = cityName, Nav5Days = {lat, lon -> navController.navigate(route = "5days/$lat/$lon")})
            }
            else
            {
                Text("Invalid arguments")
            }

        }
        composable(route = "5days/{lat}/{lon}")
        {
            backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull()
            val lon = backStackEntry.arguments?.getString("lon")?.toDoubleOrNull()

            if (lat != null && lon != null)
            {
                Next5DaysScreen(lat = lat , lon = lon)
            }

        }

    }


}