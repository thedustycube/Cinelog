package com.dustycube.cinelog.ui.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.dustycube.cinelog.data.model.Movie
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.data.model.WatchStatus
import com.dustycube.cinelog.data.repository.CommonRepository
import com.dustycube.cinelog.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    commonRepository: CommonRepository
) : ViewModel() {
    val watchlist = homeRepository.getHomeWatchlist()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pagedMovies = homeRepository.getTrendingMoviesPagingFlow()
        .cachedIn(viewModelScope)

    val trendingMovies: Flow<PagingData<Movie>> = combine(
        pagedMovies,
        commonRepository.getFullWatchlist()
    ) { pagingData, watchlistItems ->
        pagingData.map { pagedMovie ->
            val savedItem = watchlistItems.find { it.id == pagedMovie.id }
            pagedMovie.copy(watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE)
        }
    }

    val pagedTvShows = homeRepository.getTrendingTvShowsPagingFlow()
        .cachedIn(viewModelScope)

    val trendingTvShows: Flow<PagingData<TvShow>> = combine(
        pagedTvShows,
        commonRepository.getFullWatchlist()
    ) { pagingData, watchlistItems ->
        pagingData.map { pagedTvShow ->
            val savedItem = watchlistItems.find { it.id == pagedTvShow.id }
            pagedTvShow.copy(
                watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE,
                episodesWatched = savedItem?.episodesWatched ?: 0,
                number_of_episodes = savedItem?.number_of_episodes ?: 0
            )
        }
    }

    fun onUpdateWatchStatus(item: WatchItem, newStatus: WatchStatus) {
        viewModelScope.launch {
            homeRepository.updateWatchStatus(item, newStatus)
        }
    }
}
