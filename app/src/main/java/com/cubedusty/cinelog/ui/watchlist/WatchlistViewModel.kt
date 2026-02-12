package com.cubedusty.cinelog.ui.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cubedusty.cinelog.data.repository.MediaRepository
import com.cubedusty.cinelog.domain.model.UserWatchItem
import com.cubedusty.cinelog.domain.model.WatchStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class WatchlistViewModel: ViewModel() {
    val userWatchItems = MediaRepository.userWatchItems.map { items ->
        items.sortedByDescending { it.watchStatus.order }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onUpdateStatusRequest(item: UserWatchItem, newStatus: WatchStatus) {
        MediaRepository.updateWatchStatus(item, newStatus)
    }
}