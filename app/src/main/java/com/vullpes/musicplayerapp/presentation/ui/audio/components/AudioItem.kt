package com.vullpes.musicplayerapp.presentation.ui.audio.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vullpes.musicplayerapp.R
import com.vullpes.musicplayerapp.domain.model.Audio
import com.vullpes.musicplayerapp.util.MusicFunctions
import es.claucookie.miniequalizerlibrary.EqualizerView

@Composable
fun AudioItem(
    audio: Audio,
    isAudioPlaying: Boolean = false,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable {
                onItemClick()
            },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(2.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(audio.data)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.image_placeholder),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(10.dp)
                    .clip(CircleShape)
                    .size(50.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = audio.title,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Clip,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = audio.artist,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )

            }

            if (audio.isSelectedTrack){
                AndroidView(
                    factory = {
                        EqualizerView(it , null).apply {
                            animateBars()
                        }
                    },
                    update = { view ->
                        // View's been inflated or state read in this block has been updated
                        // Add logic here if necessary

                        // As selectedItem is read here, AndroidView will recompose
                        // whenever the state changes
                        // Example of Compose -> View communication
                        if (!isAudioPlaying){
                            view.stopBars()
                        }else{
                            view.animateBars()
                        }
                    },
                    modifier = Modifier.size(50.dp)
                )
            }else{
                Text(
                    text = MusicFunctions.timeStampToDuration(audio.duration.toLong())
                )
            }

            Spacer(modifier = Modifier.size(8.dp))
        }

    }
}