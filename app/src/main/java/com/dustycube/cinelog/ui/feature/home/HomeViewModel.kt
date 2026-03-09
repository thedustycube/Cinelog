package com.dustycube.cinelog.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dustycube.cinelog.data.models.Movie
import com.dustycube.cinelog.data.models.TvShow
import com.dustycube.cinelog.data.models.UserWatchItem
import com.dustycube.cinelog.data.models.WatchStatus
import com.dustycube.cinelog.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    val watchlist = homeRepository.getHomeWatchlist()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingMovies: StateFlow<List<Movie>> = homeRepository.getTrendingMoviesWithStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingTvShows: StateFlow<List<TvShow>> = homeRepository.getTrendingTvShowsWithStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onUpdateWatchStatus(item: UserWatchItem, newStatus: WatchStatus) {
        viewModelScope.launch {
            homeRepository.updateWatchStatus(item, newStatus)
        }
    }
}
