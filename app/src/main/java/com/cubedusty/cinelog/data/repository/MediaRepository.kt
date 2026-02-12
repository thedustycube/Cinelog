package com.cubedusty.cinelog.data.repository

import com.cubedusty.cinelog.data.local.datasource.MediaDataSource
import com.cubedusty.cinelog.domain.model.Movie
import com.cubedusty.cinelog.domain.model.TvShow
import com.cubedusty.cinelog.domain.model.UserWatchItem
import com.cubedusty.cinelog.domain.model.WatchStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

object MediaRepository {

    private var _userWatchItems = MutableStateFlow(MediaDataSource.getUserWatchItems().toList())
    val userWatchItems: StateFlow<List<UserWatchItem>> = _userWatchItems.asStateFlow()

    private var _trendingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val trendingMovies: StateFlow<List<Movie>> = _trendingMovies.asStateFlow()

    private var _trendingTvShows = MutableStateFlow<List<TvShow>>(emptyList())
    val trendingTvShows: StateFlow<List<TvShow>> = _trendingTvShows.asStateFlow()

    init {
        syncTrendingWithWatchlist()
    }

    fun syncTrendingWithWatchlist() {
        val userWatchlist = _userWatchItems.value

        _trendingMovies.value = MediaDataSource.getWeeklyTrendingMovies().map { movie ->
            val updatedMovie = userWatchlist.find { it.id == movie.id } as? Movie
            updatedMovie ?: movie
        }
        _trendingTvShows.value = MediaDataSource.getWeeklyTrendingTvShows().map { tvShow ->
            val updatedTvShow = userWatchlist.find { it.id == tvShow.id } as? TvShow
            updatedTvShow ?: tvShow
        }
    }

    fun addToWatchlist(item: UserWatchItem, newStatus: WatchStatus) {
        val userWatchItems = _userWatchItems.value.toMutableList()
        val index = userWatchItems.indexOfFirst { it.id == item.id }

        if (index != -1) {
            val updatedItem = when (val item = userWatchItems[index]) {
                is Movie -> item.copy(watchStatus = newStatus, lastUpdatedTimeStamp = LocalDateTime.now())
                is TvShow -> item.copy(watchStatus = newStatus, lastUpdatedTimeStamp = LocalDateTime.now())
                else -> item
            }
            userWatchItems[index] = updatedItem
        } else {
            userWatchItems.add(
                when (item) {
                    is Movie -> item.copy(watchStatus = newStatus, lastUpdatedTimeStamp = LocalDateTime.now())
                    is TvShow -> item.copy(watchStatus = newStatus, lastUpdatedTimeStamp = LocalDateTime.now())
                    else -> item
                }
            )
        }
        _userWatchItems.value = userWatchItems.toList()

        syncTrendingWithWatchlist()
    }

    fun removeFromWatchlist(item: UserWatchItem) {
        val userWatchItems = _userWatchItems.value.toMutableList()
        userWatchItems.removeAll { it.id == item.id }
        _userWatchItems.value = userWatchItems.toList()

        syncTrendingWithWatchlist()
    }

    fun updateWatchStatus(item: UserWatchItem, newStatus: WatchStatus) {
        if(newStatus != WatchStatus.NONE) {
            addToWatchlist(item, newStatus)
        } else {
            removeFromWatchlist(item)
        }
    }
}