package com.dustycube.cinelog.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dustycube.cinelog.R
import com.dustycube.cinelog.data.model.Movie
import com.dustycube.cinelog.data.model.SearchItem
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.data.model.WatchItem
import com.dustycube.cinelog.data.model.WatchStatus

fun isMovieOrTvShow(item: WatchItem): String? {
    return when (item) {
        is Movie -> item.title
        is TvShow -> item.name
        is SearchItem -> if (item.media_type == "movie") item.title else item.name
        else -> null
    }
}

@Composable
fun CardPoster(
    item: WatchItem,
    onUpdateWatchStatus: (WatchItem, WatchStatus) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    item.poster_path?.takeIf { it.isNotEmpty() }?.let { "https://image.tmdb.org/t/p/w500$it" }
                )
                .build(),
            contentDescription = isMovieOrTvShow(item),
            placeholder = painterResource(R.drawable.no_poster),
            error = painterResource(R.drawable.no_poster),
            fallback = painterResource(R.drawable.no_poster),
            modifier = Modifier.fillMaxWidth(),
                // .clip(RoundedCornerShape(8.dp))
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clip(
                    RoundedCornerShape(
                        bottomStart = 8.dp
                    )
                )
                .background(Color(0xFFE8E2DB))
                .padding(2.dp)
                .clickable { showMenu = true }
        ) {
            Icon(
                imageVector = item.watchStatus.icon,
                contentDescription = item.watchStatus.name
                // tint = Color(0xFFE8E2DB)
            )
            DropdownMenu(
                modifier = Modifier,
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                WatchStatus.entries.forEach { status ->
                    DropdownMenuItem(
                        text = {
                            Text(status.name)
                        },
                        onClick = {
                            onUpdateWatchStatus(item, status)
                            showMenu = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CardTitle(
    item: WatchItem
) {
    Row(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth()
            .background(Color.Gray)
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp),
            text = isMovieOrTvShow(item) ?: "",
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun CardBuilder(
    item: WatchItem,
    onUpdateWatchStatus: (WatchItem, WatchStatus) -> Unit,
    isHorizontal: Boolean,
    onCardClick: (WatchItem) -> Unit
) {
    if(isHorizontal) {
        Card(
            modifier = Modifier
                .height(200.dp)
                .width(120.dp)
                .clickable { onCardClick(item) }
        ) {
            CardPoster(item, onUpdateWatchStatus)
            // CardTitle(item)
        }
        Spacer(modifier = Modifier.width(4.dp))
    } else {
        Card(
            modifier = Modifier
                .height(200.dp)
                .width(120.dp)
                .clickable { onCardClick(item) }
        ) {
            CardPoster(item, onUpdateWatchStatus)
            // CardTitle(item)
        }
    }
}

@Composable
fun BannerHeader(
    bannerName: String,
    onHeaderClick: () -> Unit = { },
    hasIcon: Boolean
) {
    Row(
        modifier = Modifier
            .height(40.dp)
            .background(Color(0xFF547792))
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = bannerName,
            fontWeight = FontWeight.SemiBold
        )
        if (hasIcon) {
            IconButton(
                onClick = onHeaderClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "View all"
                )
            }
        }
    }
}

@Composable
fun HorizontalList(
    bannerName: String,
    watchItems: List<WatchItem>,
    onUpdateWatchStatus: (WatchItem, WatchStatus) -> Unit,
    onHeaderClick: () -> Unit,
    hasIcon: Boolean,
    onCardClick: (WatchItem) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        BannerHeader(bannerName, onHeaderClick, hasIcon)
        LazyRow(
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        ) {
            items(watchItems) { item ->
                CardBuilder(item = item, onUpdateWatchStatus = onUpdateWatchStatus, isHorizontal = true, onCardClick = onCardClick)
            }
        }
    }
}

@Composable
fun VerticalList(
    bannerName: String,
    watchItems: List<WatchItem>,
    onUpdateWatchStatus: (WatchItem, WatchStatus) -> Unit,
    onHeaderClick: () -> Unit,
    hasIcon: Boolean,
    onCardClick: (WatchItem) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        BannerHeader(bannerName, onHeaderClick, hasIcon)
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(watchItems) { item ->
                CardBuilder(item = item, onUpdateWatchStatus = onUpdateWatchStatus, isHorizontal = false, onCardClick = onCardClick)
            }
        }
    }
}

@Composable
fun BannerAndCardBuilder(
    bannerName: String,
    watchItems: List<WatchItem>,
    onUpdateWatchStatus: (WatchItem, WatchStatus) -> Unit,
    onHeaderClick: () -> Unit = { },
    isHorizontal: Boolean = true,
    hasIcon: Boolean = true,
    onCardClick: (WatchItem) -> Unit = { }
) {
    if(isHorizontal) HorizontalList(
        bannerName = bannerName,
        watchItems = watchItems,
        onUpdateWatchStatus = onUpdateWatchStatus,
        onHeaderClick = onHeaderClick,
        hasIcon = hasIcon,
        onCardClick = onCardClick)
    else VerticalList(
        bannerName = bannerName,
        watchItems = watchItems,
        onUpdateWatchStatus = onUpdateWatchStatus,
        onHeaderClick = onHeaderClick,
        hasIcon = hasIcon,
        onCardClick = onCardClick
    )
}