package com.dustycube.cinelog.ui.home

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dustycube.cinelog.switchTab
import com.dustycube.cinelog.ui.components.BannerAndCardBuilder
import com.dustycube.cinelog.ui.navigation.Routes


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val watchlist by viewModel.watchlist.collectAsState()
    val trendingMovies by viewModel.trendingMovies.collectAsState()
    val trendingTvShows by viewModel.trendingTvShows.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFE8E2DB))
            .verticalScroll(rememberScrollState())
    ) {
        if(trendingMovies.isEmpty() && trendingTvShows.isEmpty()) {
            Text(text = "Fetching trending movies and shows...")
            CircularProgressIndicator()
        } else {
            BannerAndCardBuilder("Watchlist",
                watchlist,
                viewModel::onUpdateWatchStatus,
                onHeaderClick = {
                    navController.switchTab(Routes.watchlist)
                }
            )
            Spacer(modifier = Modifier.height(48.dp))
//            BannerAndCardBuilder("Trending Movies", trendingMovies, viewModel::onUpdateWatchStatus)
//            Spacer(modifier = Modifier.height(48.dp))
//            BannerAndCardBuilder("Trending TV Shows", trendingTvShows, viewModel::onUpdateWatchStatus)
//            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}
