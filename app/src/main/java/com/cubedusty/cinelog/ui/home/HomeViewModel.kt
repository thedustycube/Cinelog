package com.cubedusty.cinelog.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cubedusty.cinelog.data.repository.MediaRepository
import com.cubedusty.cinelog.domain.model.UserWatchItem
import com.cubedusty.cinelog.domain.model.WatchStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel : ViewModel() {
    val userWatchItems = MediaRepository.userWatchItems.map { items ->
        items.filter {
            it.watchStatus == WatchStatus.WATCHING || it.watchStatus == WatchStatus.REWATCHING
        }.sortedByDescending { it.lastUpdatedTimeStamp }
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val trendingMovies = MediaRepository.trendingMovies
    val trendingTvShows = MediaRepository.trendingTvShows
    fun onUpdateStatusRequest(item: UserWatchItem, newStatus: WatchStatus) {
        MediaRepository.updateWatchStatus(item, newStatus)
    }
}
