package com.dustycube.cinelog.ui.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dustycube.cinelog.data.model.Movie
import com.dustycube.cinelog.data.model.Season
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.data.model.WatchStatus
import com.dustycube.cinelog.data.repository.CommonRepository
import com.dustycube.cinelog.data.repository.MediaDetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaDetailsViewModel @Inject constructor(
    private val commonRepository: CommonRepository,
    savedStateHandle: SavedStateHandle,
    private val mediaDetailsRepository: MediaDetailsRepository,
) : ViewModel() {
    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()
    fun updateTabIndex(index: Int) { _selectedTabIndex.value = index }

    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])
    private val mediaType: String = checkNotNull(savedStateHandle["media_type"])

    val uiState: StateFlow<DetailUiState> = combine(
        flow { emit(commonRepository.getItemById(itemId, mediaType)) },
        commonRepository.getFullWatchlist(),
        mediaDetailsRepository.getSeasonProgress(itemId)
    ) { item, watchlistItems, seasonProgress ->
        val savedItem = watchlistItems.find { it.id == itemId }

        val synchronizedItem = when (item) {
            is TvShow -> {
                val syncedSeasons = item.seasons.map { apiSeason ->
                    val localData =
                        seasonProgress.find { it.seasonNumber == apiSeason.season_number }
                    apiSeason.copy(
                        showId = itemId,
                        episodeWatched = localData?.episodeWatched ?: 0,
                        watchStatus = localData?.watchStatus ?: WatchStatus.NONE
                    )
                }
                item.copy(
                    watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE,
                    seasons = syncedSeasons,
                    episodesWatched = savedItem?.episodeWatched ?: 0
                )
            }
            is Movie -> item.copy(watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE)
            else -> null
        }

        if (synchronizedItem != null) DetailUiState.Success(synchronizedItem)
        else DetailUiState.Error("Item not found")
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DetailUiState.Loading
    )


    fun onUpdateWatchStatus(item: WatchItem, newStatus: WatchStatus) {
        viewModelScope.launch {
            commonRepository.updateWatchStatus(item, newStatus)
        }
    }

    fun updateSeasonStatus(season: Season, newStatus: WatchStatus, progress: Int, show: TvShow, updatedValue: Int = 0) {
        viewModelScope.launch {
            mediaDetailsRepository.updateSeasonProgress(
                season = season,
                newStatus = newStatus,
                progress = progress,
                show = show,
                updatedValue = updatedValue
            )
        }
    }
}

sealed class DetailUiState {
    object Loading: DetailUiState()
    data class Success(val item: WatchItem?) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}