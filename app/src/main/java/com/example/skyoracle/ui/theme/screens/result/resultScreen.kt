package com.example.skyoracle.ui.theme.screens.result

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun  resultScreen(
    lat: Double,
    lon: Double,

) {

    Column() {
        Text("Hi I'm RESULT SCREEN")
        Text("HI I am lat${lat}")
        Text("HI I am lon${lon}")
    }
}