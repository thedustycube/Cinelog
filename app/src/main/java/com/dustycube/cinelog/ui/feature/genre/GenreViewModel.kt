package com.dustycube.cinelog.ui.feature.genre

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dustycube.cinelog.data.models.Genre
import com.dustycube.cinelog.data.models.Movie
import com.dustycube.cinelog.data.models.TvShow
import com.dustycube.cinelog.data.models.UserWatchItem
import com.dustycube.cinelog.data.models.WatchStatus
import com.dustycube.cinelog.data.repository.GenreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val genreRepository: GenreRepository
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

    private val _moviesByGenre = MutableStateFlow<List<Movie>>(emptyList())
    val moviesByGenre: StateFlow<List<Movie>> = _moviesByGenre.asStateFlow()

    private val _tvShowsByGenre = MutableStateFlow<List<TvShow>>(emptyList())
    val tvShowsByGenre: StateFlow<List<TvShow>> = _tvShowsByGenre.asStateFlow()

    init {
        loadGenres()
    }

    fun updateTabIndex(index: Int) { _selectedTabIndex.value = index }
    fun updateMovieGenre(genre: Genre?) { _selectedMovieGenre.value = genre }
    fun updateTvShowGenre(genre: Genre?) { _selectedTvShowGenre.value = genre }

    fun getMoviesByGenre(genreId: Int) {
        viewModelScope.launch {
            genreRepository.getMoviesByGenreWithStatus(genreId)
                .collect { results ->
                    _moviesByGenre.value = results
                    Log.d("GenreViewModel", "${results.size}")
                }
        }
    }

    fun getTvShowsByGenre(genreId: Int) {
        viewModelScope.launch {
            genreRepository.getTvShowsByGenreWithStatus(genreId)
                .collect { results ->
                    _tvShowsByGenre.value = results
                }
        }
    }

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