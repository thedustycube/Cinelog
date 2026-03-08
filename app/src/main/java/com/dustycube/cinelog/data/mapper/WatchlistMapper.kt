package com.dustycube.cinelog.data.mapper

import com.dustycube.cinelog.data.local.WatchlistItemEntity
import com.dustycube.cinelog.data.models.Movie
import com.dustycube.cinelog.data.models.SearchItem
import com.dustycube.cinelog.data.models.TvShow
import com.dustycube.cinelog.data.models.WatchStatus
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
        vote_average = this.vote_average,
        vote_count = this.vote_count ?: 0,
        media_type = this.media_type ?: "",
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

fun SearchItem.toEntity(newStatus: WatchStatus): WatchlistItemEntity {
    return WatchlistItemEntity(
        id = this.id,
        title = if(this.media_type == "movie") this.title else null,
        name = if(this.media_type == "tv") this.name else null,
        adult = this.adult ?: false,
        overview = this.overview ?: "",
        popularity = this.popularity ?: 0.0,
        poster_path = this.poster_path ?: "",
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