package com.dustycube.cinelog.ui.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dustycube.cinelog.data.model.TvShow

@Composable
fun TvShowDetailsHelper(
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