package com.dustycube.cinelog.data.model

import java.time.LocalDateTime

data class TvShow(
    override val id: Int,
    val name: String?,
    override val adult: Boolean?,
    override val overview: String?,
    override val popularity: Double?,
    override val poster_path: String?,
    override val original_language: String?,
    val number_of_seasons: Int?,
    val number_of_episodes: Int?,
    val episodesWatched: Int = 0,
    val first_air_date: String?,
    override val vote_average: Double?,
    override val vote_count: Int?,
    override val media_type: String?,
    override val watchStatus: WatchStatus,
    override val lastUpdatedTimeStamp: LocalDateTime,
    val seasons: List<Season>
) : WatchItem

data class TrendingTvResponse(
    val results: List<TvShow>
)