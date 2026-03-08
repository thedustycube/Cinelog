package com.dustycube.cinelog.di

import android.util.Log
import com.dustycube.cinelog.BuildConfig
import com.dustycube.cinelog.data.api.TMDBApiService
import com.dustycube.cinelog.data.local.WatchlistDao
import com.dustycube.cinelog.data.local.WatchlistItemEntity
import com.dustycube.cinelog.data.mapper.toEntity
import com.dustycube.cinelog.data.models.Movie
import com.dustycube.cinelog.data.models.SearchItem
import com.dustycube.cinelog.data.models.TvShow
import com.dustycube.cinelog.data.models.UserWatchItem
import com.dustycube.cinelog.data.models.WatchStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class RepositoryModule(
    private val apiService: TMDBApiService,
    private val dao: WatchlistDao
) {
    val accessToken = BuildConfig.TMDB_ACCESS_TOKEN

    fun getHomeWatchlist(): Flow<List<WatchlistItemEntity>> = dao.getHomeWatchlist()

    fun getFullWatchlist(): Flow<List<WatchlistItemEntity>> = dao.getFullWatchlist()

    fun getTrendingMoviesWithStatus(): Flow<List<Movie>> = combine(
        flow { emit(fetchTrendingMovies()) },
        getFullWatchlist()
    ) {
        trendingMovies, watchlist ->
        trendingMovies.map { movie ->
            val savedItem = watchlist.find { it.id == movie.id }
            movie.copy(watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE)
        }
    }

    fun getTrendingTvShowsWithStatus(): Flow<List<TvShow>> = combine(
        flow { emit(fetchTrendingTvShows()) },
        getFullWatchlist()
    ) { trending, watchlist ->
        trending.map { tvShow ->
            val savedItem = watchlist.find { it.id == tvShow.id }
            tvShow.copy(watchStatus = savedItem?.watchStatus ?: WatchStatus.NONE)
        }
    }

    suspend fun fetchTrendingMovies(): List<Movie> {
        return try {
            val response = apiService.getTrendingMovies(token = accessToken)
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
            emptyList()
        }
    }

    suspend fun fetchTrendingTvShows(): List<TvShow> {
        return try {
            val response = apiService.getTrendingTvShows(token = accessToken)
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

    suspend fun fetchSearchResults(query: String): List<SearchItem> {
        return try {
            val response = apiService.getSearchResults(query = query, token = accessToken)
            response.results.map { searchItem ->
                SearchItem(
                    id = searchItem.id,
                    title = if (searchItem.media_type == "movie") searchItem.title else null,
                    name = if (searchItem.media_type == "tv") searchItem.name else null,
                    adult = searchItem.adult ?: false,
                    overview = searchItem.overview ?: "",
                    popularity = searchItem.popularity ?: 0.0,
                    poster_path = searchItem.poster_path,
                    vote_average = searchItem.vote_average ?: 0.0,
                    vote_count = searchItem.vote_count ?: 0,
                    media_type = searchItem.media_type ?: "",
                    watchStatus = WatchStatus.NONE,
                    lastUpdatedTimeStamp = LocalDateTime.now()
                )
            }
        } catch (e: Exception) {
            Log.e("RepositoryModule", "Error fetching search results: ${e.message}")
            emptyList()
        }
    }

    suspend fun updateWatchStatus(
        item: UserWatchItem, newStatus: WatchStatus
    ) {
        val entity = when(item) {
            is Movie -> item.toEntity(newStatus)
            is TvShow -> item.toEntity(newStatus)
            is WatchlistItemEntity -> item.copy(
                watchStatus = newStatus,
                lastUpdatedTimeStamp = LocalDateTime.now()
            )
            else -> return
        }
        if (newStatus != WatchStatus.NONE) dao.upsertItem(entity)
        else dao.removeFromWatchlist(item.id)
    }
}