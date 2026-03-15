package com.dustycube.cinelog.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dustycube.cinelog.BuildConfig
import com.dustycube.cinelog.data.api.TMDBApiService
import com.dustycube.cinelog.data.local.WatchlistDao
import com.dustycube.cinelog.data.local.WatchlistItemEntity
import com.dustycube.cinelog.data.models.Movie
import com.dustycube.cinelog.data.models.TvShow
import com.dustycube.cinelog.data.models.UserWatchItem
import com.dustycube.cinelog.data.models.WatchStatus
import com.dustycube.cinelog.data.paging.UniversalPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class HomeRepository(
    private val api: TMDBApiService,
    private val dao: WatchlistDao,
    private val commonRepository: CommonRepository
) {
    val accessToken = commonRepository.fetchAccessToken()

    fun getHomeWatchlist(): Flow<List<WatchlistItemEntity>> = dao.getHomeWatchlist()

    fun getTrendingMoviesPagingFlow(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                UniversalPagingSource { page ->
                    val response = api.getTrendingMovies(page = page, token = accessToken)
                    response.results.map { trendingMovie ->
                        trendingMovie.copy(
                            watchStatus = WatchStatus.NONE,
                            lastUpdatedTimeStamp = LocalDateTime.now()
                        )
                    }
                }
            }
        ).flow
    }

    fun getTrendingTvShowsPagingFlow(): Flow<PagingData<TvShow>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                UniversalPagingSource { page ->
                    val response = api.getTrendingTvShows(page = page, token = accessToken)
                    response.results.map { trendingTvShow ->
                        trendingTvShow.copy(
                            watchStatus = WatchStatus.NONE,
                            lastUpdatedTimeStamp = LocalDateTime.now()
                        )
                    }
                }
            }
        ).flow
    }

    suspend fun updateWatchStatus(
        item: UserWatchItem,
        newStatus: WatchStatus
    ) {
        commonRepository.updateWatchStatus(item, newStatus)
    }
}