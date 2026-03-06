package com.dustycube.cinelog.ui.features.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dustycube.cinelog.data.models.UserWatchItem
import com.dustycube.cinelog.data.models.WatchStatus
import com.dustycube.cinelog.di.RepositoryModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val remoteRepository: RepositoryModule
) : ViewModel() {
    val watchlist = remoteRepository.getFullWatchlist()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onUpdateWatchStatus(item: UserWatchItem, newStatus: WatchStatus) {
        viewModelScope.launch {
            remoteRepository.updateWatchStatus(item, newStatus)
        }
    }
}