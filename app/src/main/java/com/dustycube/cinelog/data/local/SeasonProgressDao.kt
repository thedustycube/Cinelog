package com.dustycube.cinelog.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

interface SeasonProgressDao {
    @Query("SELECT * FROM season_progress WHERE showId = :showId")
    fun getSeasonProgressForShow(showId: Int): Flow<List<SeasonProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSeasonProgress(item: SeasonProgressEntity)

    @Query("DELETE FROM season_progress WHERE showId = :showId")
    suspend fun removeSeasonProgressForShow(showId: Int)
}