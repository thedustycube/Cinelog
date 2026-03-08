package com.dustycube.cinelog.data.api

import com.dustycube.cinelog.data.models.SearchResponse
import com.dustycube.cinelog.data.models.TrendingMovieResponse
import com.dustycube.cinelog.data.models.TrendingTvResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface TMDBApiService {

    // trending movies
    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("language") lang: String = "en-US",
        @Header("Authorization") token: String
    ): TrendingMovieResponse

    // trending TV shows
    @GET("trending/tv/week")
    suspend fun getTrendingTvShows(
        @Query("language") lang: String = "en-US",
        @Header("Authorization") token: String
    ): TrendingTvResponse

    // searching for movies and TV shows
    @GET("search/multi")
    suspend fun getSearchResults(
        @Query("query") query: String,
        @Header("Authorization") token: String
    ): SearchResponse
}