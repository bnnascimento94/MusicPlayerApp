package com.vullpes.musicplayerapp.presentation.ui.audio

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vullpes.musicplayerapp.domain.model.Audio
import com.vullpes.musicplayerapp.presentation.ui.audio.components.AudioItem
import com.vullpes.musicplayerapp.presentation.ui.audio.components.BottomBarPlayer
import com.vullpes.musicplayerapp.presentation.ui.audio.components.ShimmerBottomBar
import com.vullpes.musicplayerapp.presentation.ui.audio.components.ShimmerList
import com.vullpes.musicplayerapp.presentation.ui.theme.MusicPlayerAppTheme

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
            if(audiList.isEmpty()){
                ShimmerBottomBar()
            }else{
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
                Audio(uri ="", displayName = "Title One", id= "", artist = "Said", data ="", duration = 0,title = "Title One"),
                Audio(uri ="", displayName =  "Title Two", id ="", artist = "Unknown", data="", duration =  0, title ="Title two"),
            ),
            currentPlaying = Audio(uri = "", displayName =  "Title One",id = "", artist = "Said", data ="", duration =  120,title = "teste"),
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
        audio = Audio(uri = "", displayName =  "Title One",id = "", artist = "Said", data ="", duration =  0,title = "teste"),
        onNext = {},
        onStart = {},
        isAudioPlaying = true,
        onBack = {}
    )
}

