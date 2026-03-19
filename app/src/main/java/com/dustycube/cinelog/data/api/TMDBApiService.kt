package com.dustycube.cinelog.data.api

import com.dustycube.cinelog.BuildConfig
import com.dustycube.cinelog.data.model.GenreResponse
import com.dustycube.cinelog.data.model.Movie
import com.dustycube.cinelog.data.model.MovieByGenreResponse
import com.dustycube.cinelog.data.model.SearchResponse
import com.dustycube.cinelog.data.model.TrendingMovieResponse
import com.dustycube.cinelog.data.model.TrendingTvResponse
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.data.model.TvShowsByGenreResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {
    val accessToken: String
        get() = BuildConfig.TMDB_ACCESS_TOKEN

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Header("Authorization") token: String = accessToken
    ): TrendingMovieResponse

    @GET("trending/tv/week")
    suspend fun getTrendingTvShows(
        @Query("language") lang: String = "en-US",
        @Query("page") page: Int = 1,
        @Header("Authorization") token: String = accessToken
    ): TrendingTvResponse

    @GET("search/multi")
    suspend fun getSearchResults(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Header("Authorization") token: String = accessToken
    ): SearchResponse

    @GET("genre/movie/list")
    suspend fun getMovieGenres(
        @Query("language") lang: String = "en",
        @Header("Authorization") token: String = accessToken
    ): GenreResponse

    @GET("genre/tv/list")
    suspend fun getTvShowGenres(
        @Query("language") lang: String = "en",
        @Header("Authorization") token: String = accessToken
    ): GenreResponse

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("language") lang: String = "en-US",
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1,
        @Header("Authorization") token: String = accessToken
    ): MovieByGenreResponse

    @GET("discover/tv")
    suspend fun getTvShowsByGenre(
        @Query("language") lang: String = "en-US",
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int = 1,
        @Header("Authorization") token: String = accessToken
    ): TvShowsByGenreResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetailsById(
        @Path("movie_id") id: Int,
        @Header("Authorization") token: String = accessToken
    ): Movie

    @GET("tv/{series_id}")
    suspend fun getTvShowDetailsById(
        @Query("language") lang: String = "en-US",
        @Query("series_id") id: Int,
        @Header("Authorization") token: String = accessToken
    ): TvShow
}