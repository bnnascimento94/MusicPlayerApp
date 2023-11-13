package com.vullpes.musicplayerapp.presentation.ui.audio.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vullpes.musicplayerapp.R
import com.vullpes.musicplayerapp.domain.model.Audio
import com.vullpes.musicplayerapp.util.MusicFunctions

@Composable
fun BottomBarPlayer(
    progress: Float,
    progressTimeString: String,
    onProgress: (Float) -> Unit,
    audio: Audio,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {


    Row(modifier = Modifier.padding(start = 2.dp,end=8.dp,top =8.dp).background(color = Color.White), verticalAlignment = Alignment.CenterVertically) {
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
                .size(80.dp)
        )
        Column(modifier = Modifier.weight(0.7f)) {
            ArtistInfo(modifier = Modifier,audio = audio)
            Row(modifier = Modifier.height(30.dp), verticalAlignment = Alignment.CenterVertically){
                Text(text = progressTimeString, modifier = Modifier.padding(0.dp).weight(0.2f))
                Slider(modifier = Modifier.weight(0.6f), value = progress, onValueChange = { onProgress(it) }, valueRange = 0f..100f)
                Text(text = MusicFunctions.timeStampToDuration(audio.duration.toLong()), modifier = Modifier.padding(0.dp).weight(0.2f))
            }
            MediaPlayerController(
                isAudioPlaying = isAudioPlaying,
                onStart = onStart,
                onNext = onNext,
                onBack = onBack
            )
        }
    }

}



@Composable
fun ArtistInfo(
    modifier: Modifier = Modifier,
    audio: Audio
) {

    Column(modifier = Modifier.padding(top= 8.dp, start = 4.dp)) {
        Text(
            text = audio.title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleSmall,
            overflow = TextOverflow.Clip,
            maxLines = 1,
        )
        Text(
            text = audio.artist,
            fontWeight = FontWeight.Normal,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Clip,
            maxLines = 1
        )
    }



}