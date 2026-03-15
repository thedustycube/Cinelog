package com.dustycube.cinelog.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.dustycube.cinelog.ui.components.BannerAndCardBuilder
import com.dustycube.cinelog.ui.components.CardBuilder

@Composable
fun TrendingTvShowsScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val trendingTvShows = viewModel.trendingTvShows.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFE8E2DB))
    ) {
        if (trendingTvShows.itemCount == 0) {
            Text(text = "Fetching the trending movies...")
            CircularProgressIndicator()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(trendingTvShows.itemCount) { index ->
                    val searchItem = trendingTvShows[index]
                    if (searchItem != null) {
                        CardBuilder(searchItem, viewModel::onUpdateWatchStatus, false)
                    }
                }
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}