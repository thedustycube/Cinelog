package com.dustycube.cinelog.data.repository

import com.dustycube.cinelog.data.local.WatchlistItemEntity
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.data.model.WatchStatus
import kotlinx.coroutines.flow.Flow

class WatchlistRepository(
    private val commonRepository: CommonRepository
) {
    fun getFullWatchlist(): Flow<List<WatchlistItemEntity>> = commonRepository.getFullWatchlist()

    suspend fun updateWatchStatus(
        item: WatchItem,
        newStatus: WatchStatus
    ) {
        commonRepository.updateWatchStatus(item, newStatus)
    }
}