package com.dustycube.cinelog.di

import com.dustycube.cinelog.data.api.TMDBApiService
import com.dustycube.cinelog.data.local.WatchlistDao
import com.dustycube.cinelog.data.repository.CommonRepository
import com.dustycube.cinelog.data.repository.GenreRepository
import com.dustycube.cinelog.data.repository.HomeRepository
import com.dustycube.cinelog.data.repository.SearchRepository
import com.dustycube.cinelog.data.repository.WatchlistRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideTMDBApi(): TMDBApiService {

        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TMDBApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCommonRepository(api: TMDBApiService, dao: WatchlistDao): CommonRepository {
        return CommonRepository(api, dao)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(api: TMDBApiService, dao: WatchlistDao, commonRepository: CommonRepository): HomeRepository { // added dao parameter
        return HomeRepository(api, dao, commonRepository)
    }

    @Provides
    @Singleton
    fun provideWatchlistRepository(commonRepository: CommonRepository): WatchlistRepository {
        return WatchlistRepository(commonRepository)
    }

    @Provides
    @Singleton
    fun provideSearchRepository(api: TMDBApiService, commonRepository: CommonRepository): SearchRepository {
        return SearchRepository(api, commonRepository)
    }

    @Provides
    @Singleton
    fun provideGenreRepository(api: TMDBApiService, commonRepository: CommonRepository): GenreRepository {
        return GenreRepository(api, commonRepository)
    }
}