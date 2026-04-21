package com.dustycube.cinelog.data.mapper

import com.dustycube.cinelog.data.local.WatchlistItemEntity
import com.dustycube.cinelog.data.model.Movie
import com.dustycube.cinelog.data.model.SearchItem
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.data.model.WatchStatus
import java.time.LocalDateTime

fun Movie.toEntity(newStatus: WatchStatus): WatchlistItemEntity {
    return WatchlistItemEntity(
        id = this.id,
        title = this.title,
        name = null,
        adult = this.adult ?: false,
        overview = this.overview ?: "",
        popularity = this.popularity ?: 0.0,
        poster_path = this.poster_path ?: "",
        original_language = this.original_language ?: "",
        number_of_seasons = null,
        number_of_episodes = null,
        vote_average = this.vote_average,
        vote_count = this.vote_count ?: 0,
        media_type = "movie",
        watchStatus = newStatus,
        lastUpdatedTimeStamp = LocalDateTime.now(),
        director = this.director,
        release_date = this.release_date,
        watchStatusOrder = newStatus.order
    )
}

fun TvShow.toEntity(newStatus: WatchStatus): WatchlistItemEntity {
    return WatchlistItemEntity(
        id = this.id,
        title = null,
        name = this.name,
        adult = this.adult ?: false,
        overview = this.overview ?: "",
        popularity = this.popularity ?: 0.0,
        poster_path = this.poster_path ?: "",
        original_language = this.original_language ?: "",
        number_of_seasons = this.number_of_seasons ?: 0,
        number_of_episodes = this.number_of_episodes ?: 0,
        episodeWatched = this.episodesWatched,
        vote_average = this.vote_average,
        vote_count = this.vote_count ?: 0,
        media_type = "tv",
        watchStatus = newStatus,
        lastUpdatedTimeStamp = LocalDateTime.now(),
        director = null,
        release_date = null,
        watchStatusOrder = newStatus.order
    )
}

fun SearchItem.toEntity(newStatus: WatchStatus): WatchlistItemEntity {
    return WatchlistItemEntity(
        id = this.id,
        title = if(this.media_type == "movie") this.title else null,
        name = if(this.media_type == "tv") this.name else null,
        adult = this.adult ?: false,
        overview = this.overview ?: "",
        popularity = this.popularity ?: 0.0,
        poster_path = this.poster_path ?: "",
        original_language = this.original_language ?: "",
        number_of_seasons = if (this.media_type == "tv") this.number_of_seasons else null,
        number_of_episodes = if (this.media_type == "tv") this.number_of_episodes else null,
        vote_average = this.vote_average,
        vote_count = this.vote_count ?: 0,
        media_type = this.media_type ?: "",
        watchStatus = newStatus,
        lastUpdatedTimeStamp = LocalDateTime.now(),
        director = null,
        release_date = null,
        watchStatusOrder = newStatus.order
    )
}