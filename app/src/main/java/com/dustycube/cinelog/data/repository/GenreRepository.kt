package com.dustycube.cinelog.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dustycube.cinelog.data.api.TMDBApiService
import com.dustycube.cinelog.data.model.Genre
import com.dustycube.cinelog.data.model.Movie
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.data.model.WatchStatus
import com.dustycube.cinelog.data.paging.UniversalPagingSource
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class GenreRepository(
    private val api: TMDBApiService,
    private val commonRepository: CommonRepository
) {
    val accessToken = commonRepository.fetchAccessToken()

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

    fun getMoviesByGenrePagingFlow(genreId: Int): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                UniversalPagingSource { page ->
                    val response = api.getMoviesByGenre(genreId = genreId, page = page, token = accessToken)
                    response.results.map { movieByGenre ->
                        movieByGenre.copy(
                            watchStatus = WatchStatus.NONE,
                            lastUpdatedTimeStamp = LocalDateTime.now()
                        )
                    }
                }
            }
        ).flow
    }

    fun getTvShowsByGenrePagingFlow(genreId: Int): Flow<PagingData<TvShow>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                UniversalPagingSource { page ->
                    Log.d("GenreViewModel", "API CALL: Fetching TV page $page for genre $genreId")
                    val response = api.getTvShowsByGenre(genreId = genreId, page = page, token = accessToken)
                    Log.d("GenreViewModel", "API SUCCESS: Found ${response.results.size} TV shows")
                    response.results.map { tvShowByGenre ->
                        tvShowByGenre.copy(
                            watchStatus = WatchStatus.NONE,
                            lastUpdatedTimeStamp = LocalDateTime.now(),
                            seasons = emptyList()
                        )
                    }
                }
            }
        ).flow
    }

    suspend fun updateWatchStatus(
        item: WatchItem,
        newStatus: WatchStatus
    ) {
        commonRepository.updateWatchStatus(item, newStatus)
    }
}