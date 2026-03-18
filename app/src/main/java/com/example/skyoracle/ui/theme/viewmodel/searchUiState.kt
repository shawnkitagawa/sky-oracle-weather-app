package com.example.skyoracle.ui.theme.viewmodel

import com.example.skyoracle.model.GeoData

data class searchUiState(
    val query : String = "",
    val suggestion : List<GeoData>? = null,
    val errMessage: String? = null,
    val isLoading: Boolean = false,
)