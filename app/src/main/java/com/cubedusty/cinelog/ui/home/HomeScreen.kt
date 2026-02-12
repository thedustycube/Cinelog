package com.cubedusty.cinelog.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cubedusty.cinelog.ui.components.BannerAndCardBuilder

@Preview
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel()
) {
    val userWatchItems by viewModel.userWatchItems.collectAsState()
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val trendingTvShows by viewModel.trendingTvShows.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8E2DB))
            .verticalScroll(rememberScrollState())
    ) {

        BannerAndCardBuilder("Watchlist", userWatchItems, viewModel::onUpdateStatusRequest)
        Spacer(modifier = Modifier.height(48.dp))
        BannerAndCardBuilder("Trending Movies", trendingMovies, viewModel::onUpdateStatusRequest)
        Spacer(modifier = Modifier.height(48.dp))
        BannerAndCardBuilder("Trending TV Shows", trendingTvShows, viewModel::onUpdateStatusRequest)
        Spacer(modifier = Modifier.height(48.dp))
    }
}
