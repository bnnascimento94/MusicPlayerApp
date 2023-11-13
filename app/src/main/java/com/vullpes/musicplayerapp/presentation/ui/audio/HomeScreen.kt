package com.vullpes.musicplayerapp.presentation.ui.audio

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.vullpes.musicplayerapp.domain.model.Audio
import com.vullpes.musicplayerapp.presentation.ui.audio.components.AudioItem
import com.vullpes.musicplayerapp.presentation.ui.audio.components.BottomBarPlayer
import com.vullpes.musicplayerapp.presentation.ui.audio.components.MediaPlayerController
import com.vullpes.musicplayerapp.presentation.ui.audio.components.ShimmerList
import com.vullpes.musicplayerapp.presentation.ui.theme.MusicPlayerAppTheme
import com.vullpes.musicplayerapp.util.MusicFunctions.timeStampToDuration
import es.claucookie.miniequalizerlibrary.EqualizerView

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

        if(audiList.isEmpty()){
            ShimmerList()
        }else{
            LazyColumn(contentPadding = it) {
                itemsIndexed(audiList) { index, audio ->
                    AudioItem(audio = audio, isAudioPlaying = isAudioPlaying) {
                        onItemClick(index)
                    }
                }
            }
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

