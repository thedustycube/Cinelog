package com.dustycube.cinelog.data.api

import com.dustycube.cinelog.data.models.TrendingMovieResponse
import com.dustycube.cinelog.data.models.TrendingTvResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TMDBApiService {

    // trending movies
    @GET("trending/movie/day")
    suspend fun getTrendingMovies(
        @Query("language") lang: String = "en-US",
        @Header("Authorization") token: String
    ): TrendingMovieResponse

    // trending TV shows
    @GET("trending/tv/day")
    suspend fun getTrendingTvShows(
        @Query("language") lang: String = "en-US",
        @Header("Authorization") token: String
    ): TrendingTvResponse
}