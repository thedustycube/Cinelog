package com.dustycube.cinelog.data.model

import java.time.LocalDateTime

data class TvShow(
    override val id: Int,
    val name: String?,
    override val adult: Boolean?,
    override val overview: String?,
    override val popularity: Double?,
    override val poster_path: String?,
    val first_air_date: String?,
    override val vote_average: Double?,
    override val vote_count: Int?,
    override val media_type: String?,
    override val watchStatus: WatchStatus,
    override val lastUpdatedTimeStamp: LocalDateTime
) : WatchItem

// backdrop_path: String
// genre_ids: List<Int>
// origin_country: List<String>
// original_language: String
// original_name: String

data class TrendingTvResponse(
    val results: List<TvShow>
)