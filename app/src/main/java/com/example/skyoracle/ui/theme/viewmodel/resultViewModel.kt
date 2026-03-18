package com.example.skyoracle.ui.theme.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.skyoracle.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class resultViewModel(private val weatherRepository: WeatherRepository): ViewModel() {

    private val _uistate = MutableStateFlow(resultUiState())
    val uiState: StateFlow<resultUiState> = _uistate

















}