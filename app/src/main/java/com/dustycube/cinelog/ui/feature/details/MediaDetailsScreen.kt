package com.dustycube.cinelog.ui.feature.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dustycube.cinelog.data.model.Movie
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.ui.component.CardBuilder
import com.dustycube.cinelog.ui.component.StatusBox
import java.time.LocalDate

@Composable
fun MediaDetailsScreen(
    viewModel: MediaDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is DetailUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is DetailUiState.Success -> {
            val item = (uiState as DetailUiState.Success).item
            if (item != null) DetailsBlock(item, viewModel)
            else Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "you gay")
            }
        }
        is DetailUiState.Error -> {
            val message = (uiState as DetailUiState.Error).message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = message)
            }
        }
    }
}

@Composable
fun DetailsBlock(
    item: WatchItem,
    viewModel: MediaDetailsViewModel
) {
    val tabs = listOf("ABOUT", "SEASONS")
    val selectedTabIndex by viewModel.selectedTabIndex.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFFE8E2DB))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(IntrinsicSize.Max)
                .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        ) {
            CardBuilder(
                item = item,
                onUpdateWatchStatus = { _, _ -> },
                isHorizontal = false,
                onCardClick = {  },
                hasStatusBox = false
            )
            Spacer(modifier = Modifier.width(28.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = getTitleOrName(item) ?: "",
                    modifier = Modifier.weight(1f),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light,
                    maxLines = 2
                )
                PrintDateAndDirector(item)
            }
            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = Modifier.wrapContentSize()
                        .border(1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                ) {
                    StatusBox(
                        modifier = Modifier,
                        item = item,
                        onUpdateWatchStatus = viewModel::onUpdateWatchStatus
                    )
                }

            }
        }
        if (item is Movie) AboutItem(item)
        else if (item is TvShow) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color(0xFFE8E2DB)
            ) {
                tabs.forEachIndexed { index, string ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { viewModel.updateTabIndex(index) },
                        text = { Text(string, fontWeight = FontWeight.Bold) }
                    )
                }
            }
            when (selectedTabIndex) {
                0 -> {
                    AboutItem(item)
                    TvShowAboutInfo(item)
                }
                1 -> {
                    TvShowSeasons(
                        item.seasons,
                        viewModel::updateSeasonStatus
                    )
                }
            }
        }
    }
}

@Composable
fun AboutItem(item: WatchItem) {
    Card(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 20.dp)
            .height(80.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CardBlock(item)
        }
    }
    Text(
        "Overview:",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 8.dp)
    )
    Text(
        "${item.overview}",
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp)
    )
}

fun getTitleOrName(item: WatchItem): String? {
    return when (item) {
        is Movie -> item.title
        is TvShow -> item.name
        else -> ""
    }
}

@Composable
fun PrintDateAndDirector(item: WatchItem) {
    val release: LocalDate
    var director: String? = null
    when (item) {
        is Movie -> {
            release = LocalDate.parse(item.release_date)
            director = item.credits?.crew?.find { it.job == "Director" }?.name ?: "Unknown"
        }
        is TvShow -> {
            release = LocalDate.parse(item.first_air_date)
        }
        else -> return
    }
    Text(
        "${release.month} ${release.dayOfMonth}, ${release.year}",
        modifier = Modifier.padding(vertical = 4.dp)
    )
    if (director != null) {
        Text("Director: $director")
    }
}

@Composable
fun CardBlock(
    item: WatchItem
) {
    var mediaType: String
    var language: String?
    val popularity: Double? = item.popularity

    when (item) {
        is Movie -> {
            mediaType = "Movie"
            language = item.original_language ?: ""
        }
        is TvShow -> {
            mediaType = "TV"
            language = item.original_language ?: ""
        }
        else -> {
            mediaType = "unknown"
            language = null
        }
    }
    CardBlockHelper("Format", mediaType)
    if (language != null) {
        CardBlockHelper("Language", language)
    }
    CardBlockHelper("Popularity", "%.2f".format(popularity))
}

@Composable
fun CardBlockHelper(
    header: String,
    mediaType: String
) {
    Column(
        modifier = Modifier.fillMaxHeight()
            .wrapContentWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(header)
        Text(
            mediaType,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}