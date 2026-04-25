package com.dustycube.cinelog.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.data.model.WatchStatus
import java.time.LocalDateTime

@Entity(tableName = "watchlist")
data class WatchlistItemEntity(
    @PrimaryKey override val id: Int,
    override val adult: Boolean,
    override val overview: String,
    override val popularity: Double,
    override val poster_path: String?,
    override val original_language: String?,
    override val vote_average: Double?,
    override val vote_count: Int,
    override val media_type: String,
    override val watchStatus: WatchStatus,
    override val lastUpdatedTimeStamp: LocalDateTime,
    val title: String?,
    val name: String?,
    val number_of_seasons: Int?,
    val number_of_episodes: Int?,
    val episodesWatched: Int = 0,
    val director: String?,
    val release_date: String?,
    val watchStatusOrder: Int
) : WatchItem

@Entity(
    tableName = "season_progress",
    primaryKeys = ["showId", "season_number"]
)
data class SeasonProgressEntity(
    val showId: Int,
    val season_number: Int,
    var episodeWatched: Int,
    val episodeCount: Int,
    var watchStatus: WatchStatus
)

@Entity(
    tableName = "episodes",
    primaryKeys = ["id"]
)
data class EpisodeEntity(
    val id: Int,
    val showId: Int,
    val season_number: Int,
    val episode_number: Int,
    val name: String?,
    val overview: String?,
    val air_date: String?,
    val runtime: Int?,
    val still_path: String?,
    val isWatched: Boolean = false
)

data class SeasonWithEpisodes(
    @Embedded val season: SeasonProgressEntity,
    @Relation(
        parentColumn = "showId",
        entityColumn = "showId"
    )
    val episodes: List<EpisodeEntity>
) {
    val seasonEpisodes get() = episodes.filter { it.season_number == season.season_number }
}