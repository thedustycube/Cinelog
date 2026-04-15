package com.dustycube.cinelog.ui.feature.details

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dustycube.cinelog.data.model.Movie
import com.dustycube.cinelog.data.model.Season
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.data.model.WatchStatus
import com.dustycube.cinelog.data.repository.CommonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    private val commonRepository: CommonRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()
    fun updateTabIndex(index: Int) { _selectedTabIndex.value = index }

    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])
    private val mediaType: String = checkNotNull(savedStateHandle["media_type"])

    val uiState: StateFlow<DetailUiState> = combine(
        flow {
            emit(commonRepository.getItemById(itemId, mediaType))
        },
        commonRepository.getFullWatchlist()
    ) { item, watchlistItems ->
        val savedItem = watchlistItems.find { it.id == itemId }
        DetailUiState.Success(
            when (item) {
                is Movie -> item.copy(watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE)
                is TvShow -> item.copy(
                    watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE,
                    seasons = item.seasons.map { it.copy(showId = itemId) }
                )
                else -> null
            }
        )
    }
        .map<DetailUiState, DetailUiState> { it }
        .catch { e ->
            emit(DetailUiState.Error(e.message ?: "Something went wrong"))
            Log.e("MediaDetailsViewModel", "Failed to get the item. Error: $e")
        }
        .onStart { emit(DetailUiState.Loading) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailUiState.Loading)

    fun onUpdateWatchStatus(item: WatchItem, newStatus: WatchStatus) {
        viewModelScope.launch {
            commonRepository.updateWatchStatus(item, newStatus)
        }
    }

    fun updateSeasonStatus(season: Season, newStatus: WatchStatus, progress: Int) {
        if (progress == 0 || progress < 0 || progress > season.episode_count) {
            when (newStatus) {
                WatchStatus.WATCHING -> season.watchStatus = newStatus
                WatchStatus.COMPLETED -> {
                    season.watchStatus = newStatus
                    season.episodeWatched = season.episode_count
                }
                else -> {  }
            }
        } else if (progress == season.episode_count){
            season.watchStatus = WatchStatus.COMPLETED
        } else {
            season.watchStatus = WatchStatus.WATCHING
        }
    }
}

sealed class DetailUiState {
    object Loading: DetailUiState()
    data class Success(val item: WatchItem?) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}