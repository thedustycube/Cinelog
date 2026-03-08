package com.dustycube.cinelog.data.models

import com.google.gson.annotations.SerializedName

data class Genre(
    val id: Int,
    val name: String
)

data class GenreResponse(
    val genres: List<Genre>
)

data class MovieByGenreResponse(
    val results: List<Movie>
)

data class TvShowsByGenreResponse(
    val results: List<TvShow>
)