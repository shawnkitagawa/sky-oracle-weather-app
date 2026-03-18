package com.example.skyoracle.ui.theme.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import retrofit2.HttpException
import com.example.skyoracle.application.WeatherApplication
import com.example.skyoracle.data.WeatherRepository
import com.example.skyoracle.model.GeoData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class searchViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<searchUiState>(searchUiState())
    val uiState: StateFlow<searchUiState> = _uiState


    fun updateQuery(query: String)
    {
        Log.d("UpdateQuery", "set uistate query to ${query}")
        _uiState.value = _uiState.value.copy(
            query = query
        )
    }

    fun searchByCity(cityName: String) {
        if (cityName.isBlank()) {
            Log.d("searchByCity", "searchby city is blank  ${cityName}")
            _uiState.value = _uiState.value.copy(
                errMessage = "Please enter a city name",
                isLoading = false
            )
            return
        }


        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errMessage = null
            )

            try {
                val results = weatherRepository.getCordinates(cityName, 10)

                _uiState.value = _uiState.value.copy(
                    suggestion = results,
                    isLoading = false,
                    errMessage = null
                )
            } catch (e: IOException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errMessage = "Network error. Check your internet connection."
                )
                Log.e("WeatherRepo", "Network error: ${e.message}", e)
            } catch (e: HttpException) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errMessage = "HTTP error: ${e.code()}"
                )
                Log.e("WeatherRepo", "HTTP error: ${e.code()} ${e.message()}", e)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errMessage = "Unexpected error: ${e.message}"
                )
                Log.e("WeatherRepo", "Unexpected error: ${e.message}", e)
            }
        }
    }


    companion object{

        val Factory: ViewModelProvider.Factory = viewModelFactory{

            initializer {

                val application = (this[APPLICATION_KEY] as  WeatherApplication)
                val weatherRepository = application.container.weatherRepositiory

                searchViewModel(weatherRepository = weatherRepository)

            }
        }


    }
}


