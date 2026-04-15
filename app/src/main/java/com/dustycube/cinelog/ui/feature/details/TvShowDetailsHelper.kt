package com.dustycube.cinelog.ui.feature.details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dustycube.cinelog.data.model.Season
import com.dustycube.cinelog.data.model.TvShow
import com.dustycube.cinelog.data.model.WatchStatus

@Composable
fun TvShowAboutInfo(
    item: TvShow
) {
    Card(
        modifier = Modifier.wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {
        Column(
            modifier = Modifier.wrapContentHeight()
                .padding(start = 8.dp, top = 12.dp, bottom = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Total Seasons: ${item.number_of_seasons}")
            Text("Total Episodes: ${item.number_of_episodes}")
        }
    }
}

@Composable
fun TvShowSeasons(
    seasons: List<Season>,
    seasonUpdateStatus: (Season, WatchStatus, Int) -> Unit = { _, _, _ -> }
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        seasons.forEach { season ->
            Card(
                modifier = Modifier.fillMaxWidth()
                    .height(48.dp)
                    .padding(bottom = 8.dp)
                    .clickable {  },
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                shape = RoundedCornerShape(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    season.name?.let {
                        Text(
                            text = it
                        )
                    }
                    UpdateSeasonBox(
                        season = season,
                        seasonUpdateStatus = seasonUpdateStatus
                    )
                }
            }
        }
    }
}

@Composable
fun UpdateSeasonBox(
    season: Season,
    currentStatus: WatchStatus = WatchStatus.NONE,
    seasonUpdateStatus: (Season, WatchStatus, Int) -> Unit = { _, _, _ -> }
) {
    val episodesWatched = season.episodeWatched
    var selectedStatus by remember { mutableStateOf(currentStatus) }
    var showDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val seasonStatus = listOf(WatchStatus.NONE, WatchStatus.WATCHING, WatchStatus.COMPLETED)
    var progress by remember { mutableIntStateOf(episodesWatched) }
    var textFieldValue by remember { mutableStateOf(episodesWatched.toString()) }

    Box(
        modifier = Modifier.wrapContentSize()
            .background(Color.Transparent)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp))
            .clickable { showDialog = true }
    ) {
        Icon(
            modifier = Modifier.padding(2.dp),
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit"
        )
    }
    if (showDialog) {
        Dialog(
            onDismissRequest = {  }
        ) {
            Card(
                modifier = Modifier.height(200.dp)
                    .width(400.dp)
            ) {
                Column {
                    Text(
                        text = "${season.name}",
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 8.dp)
                    )
                    HorizontalDivider(
                        modifier = Modifier.height(1.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(bottom = 4.dp, start = 4.dp),
                                text = "Status"
                            )
                            TextButton(
                                onClick = { showMenu = true },
                                modifier = Modifier.fillMaxWidth()
                                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = selectedStatus.toString(),
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "",
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false }
                            ) {
                                seasonStatus.forEach { status ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(text = status.name)
                                        },
                                        onClick = {
                                            selectedStatus = status
                                            showMenu = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(bottom = 4.dp, start = 4.dp),
                                text = "Progress"
                            )
                            Row(
                                modifier = Modifier.wrapContentSize()
                                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                            ) {
                                IconButton(
                                    onClick = {
                                        progress = (progress - 1).coerceAtLeast(0)
                                        textFieldValue = progress.toString()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.RemoveCircle,
                                        contentDescription = ""
                                    )
                                }
                                BasicTextField(
                                    value = textFieldValue,
                                    onValueChange = { input ->
                                        if (input.isEmpty()) {
                                            textFieldValue = ""
                                            progress = 0
                                        } else if (input.all { it.isDigit() } && input.length <= 4) {
                                            val newValue = input.toIntOrNull() ?: 0
                                            val constrainedValue = newValue.coerceIn(0, season.episode_count)
                                            progress = constrainedValue
                                            textFieldValue = constrainedValue.toString()
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.width(40.dp)
                                        .wrapContentHeight()
                                        .align(Alignment.CenterVertically),
                                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                                    singleLine = true,
                                    decorationBox = { innerTextField ->
                                        Box(
                                            contentAlignment = Alignment.Center
                                        ) {
                                            innerTextField()
                                        }
                                    }
                                )
                                IconButton(
                                    onClick = {
                                        progress = (progress + 1).coerceAtMost(season.episode_count)
                                        textFieldValue = progress.toString()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddCircle,
                                        contentDescription = ""
                                    )
                                }
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxSize()
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        TextButton(
                            enabled = true,
                            onClick = {  },
                            colors = ButtonColors(containerColor = Color(0XFFF87272), contentColor = Color.White, disabledContentColor = Color.Transparent, disabledContainerColor = Color.Transparent),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Discard"
                            )
                        }
                        TextButton(
                            enabled = true,
                            onClick = {
                                seasonUpdateStatus(season, selectedStatus, progress)
                            },
                            colors = ButtonColors(containerColor = Color(0XFF94CBFF), contentColor = Color.White, disabledContentColor = Color.Transparent, disabledContainerColor = Color.Transparent),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Save"
                            )
                        }
                    }
                }
            }
        }
    }
}