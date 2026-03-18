package com.example.skyoracle.ui.theme.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.skyoracle.ui.theme.screens.result.resultScreen
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

            SearchScreen(oncitySelected = {lat, lon -> navController.navigate(route = "result/$lat/$lon") })
        }
        composable(route = "result/{lat}/{lon}")
        { backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull()
            val lon = backStackEntry.arguments?.getString("lon")?.toDoubleOrNull()
            if (lat != null && lon != null)
            {
                resultScreen(lat, lon)
            }
            else
            {
                Text("Invalid arguments")
            }

        }

    }


}