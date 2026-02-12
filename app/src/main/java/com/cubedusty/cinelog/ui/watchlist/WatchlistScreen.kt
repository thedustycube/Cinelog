package com.cubedusty.cinelog.ui.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cubedusty.cinelog.ui.components.BannerAndCardBuilder

@Preview
@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel = viewModel()
) {
    val userWatchItems by viewModel.userWatchItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8E2DB))
    ) {
        BannerAndCardBuilder("Watchlist", userWatchItems, viewModel::onUpdateStatusRequest, false)
    }
}