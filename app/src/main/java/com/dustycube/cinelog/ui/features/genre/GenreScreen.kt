package com.dustycube.cinelog.ui.features.genre

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dustycube.cinelog.data.models.Genre
import com.dustycube.cinelog.ui.components.BannerHeader
import com.dustycube.cinelog.ui.components.CardBuilder
import com.dustycube.cinelog.ui.components.GenreCard

@Composable
fun GenreScreen(
    viewModel: GenreViewModel = hiltViewModel()
) {
    val movieGenres by viewModel.movieGenres.collectAsState()
    val tvShowGenres by viewModel.tvShowGenres.collectAsState()
    val moviesByGenre by viewModel.moviesByGenre.collectAsState()
    val tvShowsByGenre by viewModel.tvShowsByGenre.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedMovieGenre by remember { mutableStateOf<Genre?>(null) }
    var selectedTvShowGenre by remember { mutableStateOf<Genre?>(null) }

    val currentSelectedGenre = if (selectedTabIndex == 0) selectedMovieGenre else selectedTvShowGenre

    BackHandler(enabled = currentSelectedGenre != null) {
        if (selectedTabIndex == 0) selectedMovieGenre = null
        else selectedTvShowGenre = null
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Top
    ) {
        BannerHeader(
            currentSelectedGenre?.name ?: "Browse Genres",
            hasIcon = false
        )
        TabRow(selectedTabIndex = selectedTabIndex) {
            listOf("Movies", "TV Shows").forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> if (selectedMovieGenre != null) {
                LaunchedEffect(selectedMovieGenre) {
                    viewModel.getMoviesByGenre(selectedMovieGenre!!.id)
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(moviesByGenre) { movie -> CardBuilder(movie, viewModel::onUpdateWatchStatus, false) }
                }
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                    items(movieGenres) { genre ->
                        GenreCard(genre, onCardClick = { selectedMovieGenre = genre })
                    }
                }
            }

            1 -> {
                if (selectedTvShowGenre != null) {
                    LaunchedEffect(selectedTvShowGenre) {
                        viewModel.getTvShowsByGenre(selectedTvShowGenre!!.id)
                    }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(tvShowsByGenre) { show -> CardBuilder(show, viewModel::onUpdateWatchStatus, false) }
                    }
                } else {
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        items(tvShowGenres) { genre ->
                            GenreCard(genre, onCardClick = { selectedTvShowGenre = genre })
                        }
                    }
                }
            }
        }
    }
}