package com.dustycube.cinelog.ui.features.genre

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dustycube.cinelog.data.models.Genre
import com.dustycube.cinelog.data.models.Movie
import com.dustycube.cinelog.data.models.TvShow
import com.dustycube.cinelog.data.models.UserWatchItem
import com.dustycube.cinelog.data.models.WatchStatus
import com.dustycube.cinelog.di.RepositoryModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val remoteRepository: RepositoryModule
) : ViewModel() {
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

    fun getMoviesByGenre(genreId: Int) {
        viewModelScope.launch {
            remoteRepository.getMoviesByGenreWithStatus(genreId)
                .collect { results ->
                    _moviesByGenre.value = results
                    Log.d("GenreViewModel", "${results.size}")
                }
        }
    }

    fun getTvShowsByGenre(genreId: Int) {
        viewModelScope.launch {
            remoteRepository.getTvShowsByGenreWithStatus(genreId)
                .collect { results ->
                    _tvShowsByGenre.value = results
                }
        }
    }

    fun loadGenres() {
        viewModelScope.launch {
            _movieGenres.value = remoteRepository.fetchMovieGenres()
            _tvShowGenres.value = remoteRepository.fetchTvShowGenres()
        }
    }

    fun onUpdateWatchStatus(item: UserWatchItem, newStatus: WatchStatus) {
        viewModelScope.launch {
            remoteRepository.updateWatchStatus(item, newStatus)
        }
    }
}