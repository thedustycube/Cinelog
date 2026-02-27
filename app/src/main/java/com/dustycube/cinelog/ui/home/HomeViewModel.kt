package com.dustycube.cinelog.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dustycube.cinelog.data.models.Movie
import com.dustycube.cinelog.data.models.TvShow
import com.dustycube.cinelog.di.RepositoryModule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RepositoryModule
) : ViewModel() {
    private val _trendingMovies = MutableStateFlow<List<Movie>>(emptyList())
    val trendingMovies: StateFlow<List<Movie>> = _trendingMovies.asStateFlow()

    private val _trendingTvShows = MutableStateFlow<List<TvShow>>(emptyList())
    val trendingTvShows: StateFlow<List<TvShow>> = _trendingTvShows.asStateFlow()

    init {
        loadTrendingMovies()
        loadTrendingTvShows()
    }

    private fun loadTrendingMovies() {
        viewModelScope.launch {
            val result = repository.fetchTrendingMovies()
            _trendingMovies.value = result
        }
    }

    private fun loadTrendingTvShows() {
        viewModelScope.launch {
            val result = repository.fetchTrendingTvShows()
            _trendingTvShows.value = result
        }
    }
}
