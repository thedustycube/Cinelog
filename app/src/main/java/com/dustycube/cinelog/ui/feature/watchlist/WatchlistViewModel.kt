package com.dustycube.cinelog.ui.feature.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.data.model.WatchStatus
import com.dustycube.cinelog.data.repository.WatchlistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val watchlistRepository: WatchlistRepository
) : ViewModel() {
    private val _selectedStatuses = MutableStateFlow(
        setOf(WatchStatus.WATCHING, WatchStatus.REWATCHING, WatchStatus.PLANNING)
    )
    val selectedStatuses = _selectedStatuses.asStateFlow()

    val watchlist = combine(
        watchlistRepository.getFullWatchlist(),
        _selectedStatuses
    ) { list, statuses ->
        if (statuses.isEmpty()) {
            list
        } else {
            list.filter { item -> statuses.contains(item.watchStatus) }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleFilter(status: WatchStatus) {
        _selectedStatuses.update { currentSet ->
            if (currentSet.contains(status)) {
                currentSet - status
            } else {
                currentSet + status
            }
        }
    }

    fun onUpdateWatchStatus(item: WatchItem, newStatus: WatchStatus) {
        viewModelScope.launch {
            watchlistRepository.updateWatchStatus(item, newStatus)
        }
    }
}