package com.cubedusty.cinelog.data.local.datasource

import com.cubedusty.cinelog.R
import com.cubedusty.cinelog.domain.model.Movie
import com.cubedusty.cinelog.domain.model.TvShow
import com.cubedusty.cinelog.domain.model.UserWatchItem
import com.cubedusty.cinelog.domain.model.WatchStatus
import java.time.LocalDate

object MediaDataSource {
    fun getUserWatchItems(): List<UserWatchItem> = listOf(
        Movie(5, "The Dark Knight", R.drawable.the_dark_knight, "Christopher Nolan", LocalDate.of(2008, 7, 18), 9.0, watchStatus = WatchStatus.REWATCHING),
        Movie(1, "Inception", R.drawable.inception, "Christopher Nolan", LocalDate.of(2010, 7, 16), 8.8, watchStatus = WatchStatus.PLANNING),
        TvShow(2, "Breaking Bad", R.drawable.breaking_bad, LocalDate.of(2008, 1, 20), LocalDate.of(2013, 9, 29), 9.5, watchStatus = WatchStatus.DROPPED),
        Movie(3, "Interstellar", R.drawable.interstellar, "Christopher Nolan", LocalDate.of(2014, 11, 7), 8.7, watchStatus = WatchStatus.WATCHING),
        TvShow(4, "The Bear", R.drawable.the_bear, LocalDate.of(2022, 6, 23), null, 8.8, watchStatus = WatchStatus.COMPLETED),
    )
    fun getWeeklyTrendingMovies(): List<Movie> = listOf(
        Movie(104, "The Zone of Interest", R.drawable.the_zone_of_interest, "Jonathan Glazer", LocalDate.of(2023, 12, 15), 7.8),
        Movie(101, "Dune: Part Two", R.drawable.dune_part_2, "Denis Villeneuve", LocalDate.of(2024, 3, 1), 9.0),
        Movie(102, "Oppenheimer", R.drawable.oppenheimer, "Christopher Nolan", LocalDate.of(2023, 7, 21), 8.4),
        Movie(103, "Poor Things", R.drawable.poor_things, "Yorgos Lanthimos", LocalDate.of(2023, 12, 8), 8.0),
        Movie(105, "Anatomy of a Fall", R.drawable.anatomy_of_a_fall, "Justine Triet", LocalDate.of(2023, 8, 23), 7.8)
    )
    fun getWeeklyTrendingTvShows(): List<TvShow> = listOf(
        TvShow(204, "House of the Dragon", R.drawable.house_of_the_dragon, LocalDate.of(2022, 8, 21), null, 8.4),
        TvShow(201, "Shōgun", R.drawable.shogun, LocalDate.of(2024, 2, 27), null, 9.1),
        TvShow(202, "Fallout", R.drawable.fallout, LocalDate.of(2024, 4, 10), null, 8.6),
        TvShow(203, "Blue Eye Samurai", R.drawable.blue_eye_samurai, LocalDate.of(2023, 11, 3), null, 8.8),
        TvShow(205, "The White Lotus", R.drawable.the_white_lotus, LocalDate.of(2021, 7, 11), null, 7.9)
    )
}