package com.dustycube.cinelog.ui.feature.genre

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dustycube.cinelog.ui.components.BannerHeader
import com.dustycube.cinelog.ui.components.CardBuilder
import com.dustycube.cinelog.ui.components.GenreCardBuilder

@Composable
fun GenreScreen(
    viewModel: GenreViewModel = hiltViewModel()
) {
    val movieGenres by viewModel.movieGenres.collectAsState()
    val tvShowGenres by viewModel.tvShowGenres.collectAsState()
    val tabs = listOf("Movies", "TV Shows")

    val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()
    val selectedMovieGenre by viewModel.selectedMovieGenre.collectAsState()
    val selectedTvShowGenre by viewModel.selectedTvShowGenre.collectAsState()

    val moviesByGenre by viewModel.moviesByGenre.collectAsState()
    val tvShowsByGenre by viewModel.tvShowsByGenre.collectAsState()

    BackHandler(
        enabled = selectedMovieGenre != null || selectedTvShowGenre != null
    ) {
        if (selectedTabIndex == 0) viewModel.updateMovieGenre(null)
        else viewModel.updateTvShowGenre(null)
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFE8E2DB))
    ) {
        BannerHeader(
            "Browse Genres",
            hasIcon = false
        )
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color(0xFFE8E2DB)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { viewModel.updateTabIndex(index) },
                    text = { Text(title, fontWeight = FontWeight.Bold) }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> {
                if (selectedMovieGenre != null) {
                    LaunchedEffect(selectedMovieGenre) {
                        viewModel.getMoviesByGenre(selectedMovieGenre!!.id)
                    }
                    BannerHeader(
                        selectedMovieGenre!!.name,
                        hasIcon = false
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(moviesByGenre) { movieByGenre ->
                            CardBuilder(
                                movieByGenre,
                                viewModel::onUpdateWatchStatus,
                                false
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.background(Color.Gray)
                            .padding(4.dp)
                    ) {
                        items(movieGenres) { movieGenre ->
                            GenreCardBuilder(movieGenre, onCardClick = { viewModel.updateMovieGenre(movieGenre) })
                        }
                    }
                }
            }
            1 -> {
                if (selectedTvShowGenre != null) {
                    LaunchedEffect(selectedTvShowGenre) {
                        viewModel.getTvShowsByGenre(selectedTvShowGenre!!.id)
                    }
                    BannerHeader(
                        selectedTvShowGenre!!.name,
                        hasIcon = false
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(tvShowsByGenre) { tvShowByGenre ->
                            CardBuilder(
                                tvShowByGenre,
                                viewModel::onUpdateWatchStatus,
                                false
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.background(Color.Gray)
                            .padding(4.dp)
                    ) {
                        items(tvShowGenres) { tvShowGenre ->
                            GenreCardBuilder(tvShowGenre, onCardClick = { viewModel.updateTvShowGenre(tvShowGenre) })
                        }
                    }
                }
            }
        }
    }
}