package com.dustycube.cinelog.ui.feature.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.dustycube.cinelog.data.model.Genre
import com.dustycube.cinelog.data.model.Movie
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.data.model.UserWatchItem
import com.dustycube.cinelog.data.model.WatchStatus
import com.dustycube.cinelog.data.repository.CommonRepository
import com.dustycube.cinelog.data.repository.GenreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val genreRepository: GenreRepository,
    commonRepository: CommonRepository
) : ViewModel() {

    private val _selectedTabIndex = MutableStateFlow(0)
    val selectedTabIndex = _selectedTabIndex.asStateFlow()

    private val _selectedMovieGenre = MutableStateFlow<Genre?>(null)
    val selectedMovieGenre = _selectedMovieGenre.asStateFlow()

    private val _selectedTvShowGenre = MutableStateFlow<Genre?>(null)
    val selectedTvShowGenre = _selectedTvShowGenre.asStateFlow()

    private val _movieGenres = MutableStateFlow<List<Genre>>(emptyList())
    val movieGenres: StateFlow<List<Genre>> = _movieGenres.asStateFlow()

    private val _tvShowGenres = MutableStateFlow<List<Genre>>(emptyList())
    val tvShowGenres: StateFlow<List<Genre>> = _tvShowGenres.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val pagedMovies = _selectedMovieGenre
        .filterNotNull()
        .flatMapLatest { genre ->
            genreRepository.getMoviesByGenrePagingFlow(genre.id)
        }
        .cachedIn(viewModelScope)

    val moviesByGenre: Flow<PagingData<Movie>> = combine(
        pagedMovies,
        commonRepository.getFullWatchlist()
    ) { pagingData, watchlistItems ->
        pagingData.map { pagedMovie ->
            val savedItem = watchlistItems.find { it.id == pagedMovie.id }
            pagedMovie.copy(watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val pagedTvShows = _selectedTvShowGenre
        .filterNotNull()
        .flatMapLatest { genre ->
            genreRepository.getTvShowsByGenrePagingFlow(genre.id)
        }
        .cachedIn(viewModelScope)

    val tvShowsByGenre: Flow<PagingData<TvShow>> = combine(
        pagedTvShows,
        commonRepository.getFullWatchlist()
    ) { pagingData, watchlistItems ->
        pagingData.map { pagedTvShow ->
            val savedItem = watchlistItems.find { it.id == pagedTvShow.id }
            pagedTvShow.copy(watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE)
        }
    }

    init {
        loadGenres()
    }

    fun updateTabIndex(index: Int) { _selectedTabIndex.value = index }
    fun updateMovieGenre(genre: Genre?) { _selectedMovieGenre.value = genre }
    fun updateTvShowGenre(genre: Genre?) { _selectedTvShowGenre.value = genre }

    fun loadGenres() {
        viewModelScope.launch {
            _movieGenres.value = genreRepository.fetchMovieGenres()
            _tvShowGenres.value = genreRepository.fetchTvShowGenres()
        }
    }

    fun onUpdateWatchStatus(item: UserWatchItem, newStatus: WatchStatus) {
        viewModelScope.launch {
            genreRepository.updateWatchStatus(item, newStatus)
        }
    }
}