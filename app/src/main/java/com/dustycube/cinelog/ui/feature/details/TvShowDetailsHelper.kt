package com.dustycube.cinelog.ui.feature.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dustycube.cinelog.data.model.TvShow

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
fun TvShowEpisodes(
    seasons: Int
) {
    val seasonsExpanded = remember { mutableStateMapOf<Int, Boolean>() }

    Spacer(modifier = Modifier.height(8.dp))
    for (i in 1..seasons) {
        val isExpanded = seasonsExpanded[i] == true

        Card(
            modifier = Modifier.wrapContentHeight()
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray)
        ) {
            Row(
                modifier = Modifier.height(48.dp)
                    .fillMaxWidth()
                    .padding(start = 8.dp)
                    .clickable { seasonsExpanded[i] = !isExpanded },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Season $i")
                IconButton(onClick = { seasonsExpanded[i] = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
            }
            if (isExpanded) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp)
                        .height(16.dp)
                        .fillMaxHeight()
                        .background(Color.Black)
                ) {

                }
            }
        }
    }
}