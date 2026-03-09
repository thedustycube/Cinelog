package com.dustycube.cinelog.data.repository

import android.util.Log
import com.dustycube.cinelog.data.api.TMDBApiService
import com.dustycube.cinelog.data.local.WatchlistItemEntity
import com.dustycube.cinelog.data.models.Genre
import com.dustycube.cinelog.data.models.Movie
import com.dustycube.cinelog.data.models.TvShow
import com.dustycube.cinelog.data.models.UserWatchItem
import com.dustycube.cinelog.data.models.WatchStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class GenreRepository(
    private val api: TMDBApiService,
    private val commonRepository: CommonRepository
) {
    val accessToken = commonRepository.fetchAccessToken()

    fun getFullWatchlist(): Flow<List<WatchlistItemEntity>> = commonRepository.getFullWatchlist()

    suspend fun fetchMovieGenres(): List<Genre> {
        return try {
            val response = api.getMovieGenres(token = accessToken)
            response.genres.map {
                Genre(
                    id = it.id,
                    name = it.name
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchTvShowGenres(): List<Genre> {
        return try {
            val response = api.getTvShowGenres(token = accessToken)
            response.genres.map {
                Genre(
                    id = it.id,
                    name = it.name
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getMoviesByGenreWithStatus(genreId: Int): Flow<List<Movie>> = combine(
        flow { emit(fetchMoviesByGenre(genreId)) },
        getFullWatchlist()
    ) { movies, watchlist ->
        movies.map { movie ->
            val savedItem = watchlist.find { it.id == movie.id }
            movie.copy(watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE)
        }
    }

    suspend fun fetchMoviesByGenre(genreId: Int): List<Movie> {
        return try {
            val response = api.getMoviesByGenre(genreId = genreId, token = accessToken)
            response.results.map { movie ->
                Movie(
                    id = movie.id,
                    title = movie.title,
                    adult = movie.adult,
                    overview = movie.overview,
                    popularity = movie.popularity,
                    poster_path = movie.poster_path,
                    director = "",
                    release_date = movie.release_date,
                    vote_average = movie.vote_average,
                    vote_count = movie.vote_count,
                    media_type = movie.media_type,
                    watchStatus = WatchStatus.NONE,
                    lastUpdatedTimeStamp = LocalDateTime.now()
                )
            }
        } catch (e: Exception) {
            Log.e("RepositoryModule", "Error fetching movies by genre: ${e.message}")
            emptyList()
        }
    }

    fun getTvShowsByGenreWithStatus(genreId: Int): Flow<List<TvShow>> = combine(
        flow { emit(fetchTvShowsByGenre(genreId)) },
        getFullWatchlist()
    ) { tvShows, watchlist ->
        tvShows.map { tvShow ->
            val savedItem = watchlist.find { it.id == tvShow.id }
            tvShow.copy(watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE)
        }
    }

    suspend fun fetchTvShowsByGenre(genreId: Int): List<TvShow> {
        return try {
            val response = api.getTvShowsByGenre(genreId = genreId, token = accessToken)
            response.results.map { tvShow ->
                TvShow(
                    id = tvShow.id,
                    name = tvShow.name,
                    adult = tvShow.adult,
                    overview = tvShow.overview,
                    popularity = tvShow.popularity,
                    poster_path = tvShow.poster_path,
                    vote_average = tvShow.vote_average,
                    vote_count = tvShow.vote_count,
                    media_type = tvShow.media_type,
                    watchStatus = WatchStatus.NONE,
                    lastUpdatedTimeStamp = LocalDateTime.now()
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateWatchStatus(
        item: UserWatchItem,
        newStatus: WatchStatus
    ) {
        commonRepository.updateWatchStatus(item, newStatus)
    }
}