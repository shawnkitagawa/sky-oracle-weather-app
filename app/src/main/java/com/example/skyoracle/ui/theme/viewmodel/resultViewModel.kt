package com.example.skyoracle.ui.theme.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.skyoracle.application.WeatherApplication
import com.example.skyoracle.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Clock.System.now

class resultViewModel(private val weatherRepository: WeatherRepository): ViewModel() {

    private val _uistate = MutableStateFlow(resultTodayUiState())
    val uiState: StateFlow<resultTodayUiState> = _uistate

//    fun getIconUrl(icon: String):String
//    {
//        try{
//            return "https://openweathermap.org/img/wn/${icon}@4x.png"
//        } catch(e: Exception)
//        {
//            Log.d("getIcon", "Error detected")
//        }
//        return "https://openweathermap.org/img/wn/04n@4x.png"
//
//    }
fun getWeatherIcon( iconCode: String, code: Int,): ImageVector {
    return when {
        code == 800 && iconCode.endsWith("d") -> Icons.Default.WbSunny
        code == 800 && iconCode.endsWith("n") -> Icons.Default.Nightlight

        code in 801..804 -> Icons.Default.Cloud

        code in 200..232 -> Icons.Default.Thunderstorm

        code in 300..321 -> Icons.Default.WaterDrop

        code in 500..531 -> Icons.Default.WaterDrop

        code in 600..622 -> Icons.Default.AcUnit

        code in 700..781 -> Icons.Filled.Cloud

        else -> Icons.Default.Cloud
    }
}
    fun getWeatherData(lat: Double, lon: Double)
    {
            viewModelScope.launch{

                _uistate.value = _uistate.value.copy(status = Status.LOADING)
                try {
                    Log.d("getWeatherData", "Inside the viewmodelScope ")

                    Log.d("WeatherVM", "lat=$lat, lon=$lon")

                    val weatherData = weatherRepository.getWeatherData(lat = lat, lon = lon, units = "metric", lang = "en")

                    Log.d("getWeatherData", "the weather data is ${weatherData}")


                    _uistate.value = _uistate.value.copy(
                        cityName = weatherData[0].cityName,
                        country = weatherData[0].country,
                        weather = weatherData[0].weather,
                        weatherDescription = weatherData[0].weatherDescription,
                        tempMin = weatherData[0].tempMin,
                        tempMax = weatherData[0].tempMax,
                        pop = weatherData[0].pop,
                        windSpeed = weatherData[0].windSpeed,
                        humidity = weatherData[0].humidity,
                        hourWeather = weatherData.filter { item ->
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

                            val now = LocalDateTime.now()

                            val localDateTime = LocalDateTime.parse(item.dtTxt, formatter)
                                .atZone(ZoneId.of("UTC"))
                                .withZoneSameInstant(ZoneId.systemDefault())
                                .toLocalDateTime()

                            localDateTime.toLocalDate() == now.toLocalDate()
                        }.map { item ->
                            val dateTime = dateConvertionHelper(item.dtTxt)

                            HourWeather(
                                time = dateTime.toLocalTime().toString(),
                                date = dateTime.toLocalDate().toString(),
                                temp = item.temp,
                                icon = item.icon,
                                weatherConidtion = item.weatherCondition
                            )
                        },
                        nextDaysWeather = weatherData
                            .filter { item ->
                                val dateTime = dateConvertionHelper(item.dtTxt)
                                val now = LocalDateTime.now()
                                dateTime.toLocalDate() != now.toLocalDate()
                            }
                            .groupBy { item ->
                                val dateTime = dateConvertionHelper(item.dtTxt)
                                dateTime.toLocalDate()
                            }
                            .map { (date, items) ->
                                val representative = items.find { item ->
                                    val dateTime = dateConvertionHelper(item.dtTxt)
                                    dateTime.hour == 12
                                } ?: items.first()

                                DayWeathersSummary(
                                    date = date.toString(),
                                    tempMin = items.minOf { it.temp },
                                    tempMax = items.maxOf { it.temp },
                                    temp = representative.temp,
                                    icon = representative.icon,
                                    weather = representative.weather ?: "No Weather",
                                    weatherDescription = representative.weatherDescription ?: "No Description",
                                    pop = representative.pop,
                                    windSpeed = representative.windSpeed,
                                    humidity = representative.humidity,
                                    weatherCondition = representative.weatherCondition
                                )
                            },
                        status = Status.SUCCESS
                    )
                    Log.d("Weathervm", "weatherData: ${uiState.value.OtherDayWeather}")


                }catch (e: IOException)
                {
                    Log.e("Weathervm", "Network Error", e)
                    _uistate.value = _uistate.value.copy(
                        status = Status.ERROR
                    )

                }catch (e: HttpException)
                {
                    Log.e("WeatherVm", "HTTP error: ${e.code()}: ${e.message()}", e)
                    val errorBody = e.response()?.errorBody()?.string()
                    Log.e("WeatherVM", "HTTP ${e.code()} error body: $errorBody", e)
                    _uistate.value = _uistate.value.copy(
                        status = Status.ERROR
                    )

                }catch (e: Exception)
                {
                    Log.e("WeatherVM", "Unknown Error", e)
                    _uistate.value = _uistate.value.copy(
                        status = Status.ERROR
                    )

                }
            }
    }
    companion object{

        val Factory: ViewModelProvider.Factory = viewModelFactory{

            initializer {

                val application = (this[APPLICATION_KEY] as  WeatherApplication)
                val weatherRepository = application.container.weatherRepositiory

                resultViewModel(weatherRepository = weatherRepository)

            }
        }


    }

}
fun dateConvertionHelper(dtTxt: String): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    return LocalDateTime.parse(dtTxt, formatter)
        .atZone(ZoneId.of("UTC"))
        .withZoneSameInstant(ZoneId.systemDefault())
        .toLocalDateTime()
}