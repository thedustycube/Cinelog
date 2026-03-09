package com.dustycube.cinelog.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dustycube.cinelog.ui.components.BannerAndCardBuilder

@Composable
fun TrendingTvShowsScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val trendingTvShows by viewModel.trendingTvShows.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFE8E2DB))
    ) {
        if (trendingTvShows.isEmpty()) {
            Text(text = "Fetching the trending movies...")
            CircularProgressIndicator()
        } else {
            BannerAndCardBuilder("Watchlist",
                trendingTvShows,
                viewModel::onUpdateWatchStatus,
                isHorizontal = false,
                hasIcon = false
            )
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}