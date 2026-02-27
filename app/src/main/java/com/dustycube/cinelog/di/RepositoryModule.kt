package com.dustycube.cinelog.di

import com.dustycube.cinelog.BuildConfig
import com.dustycube.cinelog.data.api.TMDBApiService
import com.dustycube.cinelog.data.models.Movie
import com.dustycube.cinelog.data.models.TvShow
import com.dustycube.cinelog.data.models.WatchStatus
import java.time.LocalDateTime

class RepositoryModule(
    private val apiService: TMDBApiService
) {
    val accessToken = BuildConfig.TMDB_ACCESS_TOKEN

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
}