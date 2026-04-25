package com.dustycube.cinelog.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.dustycube.cinelog.data.model.Episode
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist ORDER BY watchStatusOrder DESC")
    fun getFullWatchlist(): Flow<List<WatchlistItemEntity>>

    @Query("SELECT * FROM watchlist WHERE watchstatus IN ('WATCHING', 'REWATCHING') ORDER BY lastUpdatedTimeStamp DESC")
    fun getHomeWatchlist(): Flow<List<WatchlistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertItem(item: WatchlistItemEntity)

    @Query("DELETE FROM watchlist WHERE id = :id")
    suspend fun removeFromWatchlist(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSeasonProgress(progress: SeasonProgressEntity)

    @Query("SELECT * FROM season_progress WHERE showId = :showId")
    fun getSeasonProgress(showId: Int): Flow<List<SeasonProgressEntity>>

    @Query("DELETE FROM season_progress WHERE showId = :showId AND season_number = :seasonNumber")
    suspend fun deleteSeasonProgress(showId: Int, seasonNumber: Int)

    @Upsert
    suspend fun upsertAllSeasonProgress(list: List<SeasonProgressEntity>)

    @Upsert
    suspend fun upsertEpisodes(episodes: List<EpisodeEntity>)

    @Transaction
    @Query("SELECT * FROM season_progress WHERE showId = :showId AND season_number = :seasonNumber")
    fun getSeasonWithEpisodes(showId: Int, seasonNumber: Int): Flow<SeasonWithEpisodes>
}