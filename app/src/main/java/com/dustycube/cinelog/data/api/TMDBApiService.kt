package com.dustycube.cinelog.data.api

import com.dustycube.cinelog.data.models.GenreResponse
import com.dustycube.cinelog.data.models.MovieByGenreResponse
import com.dustycube.cinelog.data.models.SearchResponse
import com.dustycube.cinelog.data.models.TrendingMovieResponse
import com.dustycube.cinelog.data.models.TrendingTvResponse
import com.dustycube.cinelog.data.models.TvShowsByGenreResponse
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
        @Query("page") page: Int = 1,
        @Header("Authorization") token: String
    ): SearchResponse

    // get movie and TV show genres
    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") lang: String = "en",
        @Header("Authorization") token: String
    ): GenreResponse

    @GET("genre/tv/list")
    suspend fun getTvShowGenres(
        @Query("language") lang: String = "en",
        @Header("Authorization") token: String
    ): GenreResponse

    // get movies by genre
    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("language") lang: String = "en-US",
        @Query("with_genres") genreId: Int,
        @Header("Authorization") token: String
    ): MovieByGenreResponse

    // get TV shows by genre
    @GET("discover/tv")
    suspend fun getTvShowsByGenre(
        @Query("language") lang: String = "en-US",
        @Query("with_genres") genreId: Int,
        @Header("Authorization") token: String
    ): TvShowsByGenreResponse
}