package com.dustycube.cinelog.data.model

import java.time.LocalDateTime

data class SearchItem(
    override val id: Int,
    val title: String?,
    val name: String?,
    override val adult: Boolean?,
    override val overview: String?,
    override val popularity: Double?,
    override val poster_path: String?,
    override val original_language: String?,
    val number_of_seasons: Int?,
    val number_of_episodes: Int?,
    val episodesWatched: Int,
    override val media_type: String?,
    override val vote_average: Double?,
    override val vote_count: Int?,
    override val watchStatus: WatchStatus,
    override val lastUpdatedTimeStamp: LocalDateTime
): WatchItem

data class SearchResponse(
    val results: List<SearchItem>,
    val total_pages: Int,
    val total_results: Int
)