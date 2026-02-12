package com.cubedusty.cinelog.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cubedusty.cinelog.ui.home.HomeViewModel

@Composable
fun GenreScreen(

) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Genres",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}