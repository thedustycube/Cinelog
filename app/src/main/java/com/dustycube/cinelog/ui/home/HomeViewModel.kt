package com.dustycube.cinelog.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dustycube.cinelog.data.models.Movie
import com.dustycube.cinelog.data.models.TvShow
import com.dustycube.cinelog.data.models.UserWatchItem
import com.dustycube.cinelog.data.models.WatchStatus
import com.dustycube.cinelog.di.RepositoryModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteRepository: RepositoryModule
) : ViewModel() {
    val watchlist = remoteRepository.getHomeWatchlist()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingMovies: StateFlow<List<Movie>> = remoteRepository.getTrendingMoviesWithStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingTvShows: StateFlow<List<TvShow>> = remoteRepository.getTrendingTvShowsWithStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onUpdateWatchStatus(item: UserWatchItem, newStatus: WatchStatus) {
        viewModelScope.launch {
            remoteRepository.updateWatchStatus(item, newStatus)
        }
    }
}
