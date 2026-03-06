package com.dustycube.cinelog.ui.features.watchlist

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
fun WatchlistScreen(
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val watchlist by viewModel.watchlist.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFE8E2DB))
    ) {
        if (watchlist.isEmpty()) {
            Text(text = "Fetching user watchlist...")
            CircularProgressIndicator()
        } else {
            BannerAndCardBuilder("Watchlist",
                watchlist,
                viewModel::onUpdateWatchStatus,
                isHorizontal = false,
                hasIcon = false
            )
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}