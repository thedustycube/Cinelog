package com.dustycube.cinelog.ui.feature.watchlist

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
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.ui.component.BannerAndCardBuilder
import com.dustycube.cinelog.ui.component.MultiFilterHeader

@Composable
fun WatchlistScreen(
    onCardClick: (WatchItem) -> Unit,
    viewModel: WatchlistViewModel = hiltViewModel()
) {
    val watchlist by viewModel.watchlist.collectAsState()
    val selectedStatuses by viewModel.selectedStatuses.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFE8E2DB))
    ) {
        MultiFilterHeader(
            selectedStatuses = selectedStatuses,
            onToggleStatus = viewModel::toggleFilter
        )
        if (watchlist.isEmpty()) {
            Text(text = "Fetching user watchlist...")
            CircularProgressIndicator()
        } else {
            BannerAndCardBuilder("Watchlist",
                watchItems = watchlist,
                onUpdateWatchStatus = viewModel::onUpdateWatchStatus,
                isHorizontal = false,
                hasIcon = false,
                onCardClick = onCardClick
            )
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}