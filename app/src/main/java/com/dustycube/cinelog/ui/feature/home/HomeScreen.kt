package com.dustycube.cinelog.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.ui.component.BannerAndCardBuilder


@Composable
fun HomeScreen(
    onNavigateToWatchlist: () -> Unit,
    onNavigateToTrendingMovies: () -> Unit,
    onNavigateToTrendingTvShows: () -> Unit,
    onCardClick: (WatchItem) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val watchlist by viewModel.watchlist.collectAsState()
    val trendingMovies = viewModel.trendingMovies.collectAsLazyPagingItems()
    val trendingTvShows = viewModel.trendingTvShows.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFE8E2DB))
            .verticalScroll(rememberScrollState())
    ) {
        if(trendingMovies.itemCount == 0 && trendingTvShows.itemCount == 0) {
            Text(text = "Fetching trending movies and shows...")
            CircularProgressIndicator()
        } else {
            BannerAndCardBuilder("Watchlist",
                watchlist,
                viewModel::onUpdateWatchStatus,
                onHeaderClick = onNavigateToWatchlist,
                onCardClick = onCardClick
            )
            Spacer(modifier = Modifier.height(48.dp))
            BannerAndCardBuilder(
                "Trending Movies",
                trendingMovies.itemSnapshotList.items,
                viewModel::onUpdateWatchStatus,
                onHeaderClick = onNavigateToTrendingMovies,
                onCardClick = onCardClick
            )
            Spacer(modifier = Modifier.height(48.dp))
            BannerAndCardBuilder(
                "Trending TV Shows",
                trendingTvShows.itemSnapshotList.items,
                viewModel::onUpdateWatchStatus,
                onHeaderClick = onNavigateToTrendingTvShows,
                onCardClick = onCardClick
            )
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
