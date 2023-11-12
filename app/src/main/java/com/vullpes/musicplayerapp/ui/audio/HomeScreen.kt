package com.vullpes.musicplayerapp.ui.audio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vullpes.musicplayerapp.R
import com.vullpes.musicplayerapp.data.local.model.Audio
import com.vullpes.musicplayerapp.ui.theme.MusicPlayerAppTheme
import es.claucookie.miniequalizerlibrary.EqualizerView
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    progress: Float,
    progressTimeString:String,
    onProgress: (Float) -> Unit,
    isAudioPlaying: Boolean,
    currentPlaying: Audio,
    audiList: List<Audio>,
    onStart: () -> Unit,
    onItemClick: (Int) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {


    Scaffold(
        bottomBar = {
            BottomBarPlayer(
                progress = progress,
                progressTimeString = progressTimeString,
                onProgress = onProgress,
                audio = currentPlaying,
                isAudioPlaying = isAudioPlaying,
                onStart = onStart,
                onNext = onNext,
                onBack = onBack
            )
        }
    ) {
        LazyColumn(contentPadding = it) {
            itemsIndexed(audiList) { index, audio ->
                AudioItem(audio = audio, isAudioPlaying = isAudioPlaying) {
                    onItemClick(index)
                }
            }
        }

    }

}

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
                    text = timeStampToDuration(audio.duration.toLong())
                )
            }

            Spacer(modifier = Modifier.size(8.dp))
        }

    }
}

private fun timeStampToDuration(duration:Long):String{
    val minutes: Long = TimeUnit.MINUTES.convert(duration, TimeUnit.SECONDS)
    val seconds: Long = (duration
            - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
    return String.format("%02d:%02d", minutes, seconds)
}

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
                    Text(text = timeStampToDuration(audio.duration.toLong()), modifier = Modifier.padding(0.dp).weight(0.2f))
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
fun MediaPlayerController(
    modifier: Modifier = Modifier,
    isAudioPlaying: Boolean,
    onStart: () -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.SkipPrevious,
            modifier = Modifier.clickable {
                onBack()
            },
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(8.dp))
        PlayerIconItem(
            imgAlbum = if (isAudioPlaying) Icons.Default.Pause
            else Icons.Default.PlayArrow
        ) {
            onStart()
        }
        Spacer(modifier = Modifier.size(8.dp))
        Icon(
            imageVector = Icons.Default.SkipNext,
            modifier = Modifier.clickable {
                onNext()
            },
            contentDescription = null
        )
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

@Composable
fun PlayerIconItem(
    imgAlbum: ImageVector,
    borderStroke: BorderStroke? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Surface(
        shape = CircleShape,
        border = borderStroke,
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                onClick()
            },
        contentColor = color,
        color = backgroundColor

    ) {
        Box(modifier = Modifier.padding(4.dp), contentAlignment = Alignment.Center) {
            Icon(
                imageVector = imgAlbum,
                contentDescription = null
            )
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPrev() {
    MusicPlayerAppTheme {


        
        HomeScreen(
            progress = 50f,
            progressTimeString = "2:30",
            onProgress = {},
            isAudioPlaying = true,
            audiList = listOf(
                Audio(uri ="", displayName = "Title One", id= 0L, artist = "Said", data ="", duration = 0,title = "Title One"),
                Audio(uri ="", displayName =  "Title Two", id =0L, artist = "Unknown", data="", duration =  0, title ="Title two"),
            ),
            currentPlaying = Audio(uri = "", displayName =  "Title One",id = 0L, artist = "Said", data ="", duration =  120,title = "teste"),
            onStart = {},
            onItemClick = {},
            onNext = {},
            onBack = {}
        )
    }
}


@Preview
@Composable
fun BottomAppBarPreview() {
    BottomBarPlayer(
        progress = 0.5f,
        progressTimeString = "2:30",
        onProgress = {},
        audio = Audio(uri = "", displayName =  "Title One",id = 0L, artist = "Said", data ="", duration =  0,title = "teste"),
        onNext = {},
        onStart = {},
        isAudioPlaying = true,
        onBack = {}
    )
}

