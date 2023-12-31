package com.vullpes.musicplayerapp.presentation.ui.audio

import androidx.media3.common.MediaItem
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaMetadata
import com.vullpes.musicplayerapp.domain.model.Audio
import com.vullpes.musicplayerapp.data.player.service.JetAudioServiceHandler
import com.vullpes.musicplayerapp.data.player.service.JetAudioState
import com.vullpes.musicplayerapp.data.player.service.PlayerEvent
import com.vullpes.musicplayerapp.data.repository.MusicRepositoryImpl
import com.vullpes.musicplayerapp.domain.ListMusicUseCase
import com.vullpes.musicplayerapp.domain.MusicRepository
import com.vullpes.musicplayerapp.util.MusicFunctions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private val audioDummy = Audio(
    "","","","","",0,""

)

@HiltViewModel
class AudioViewModel @Inject constructor(
    private val audioServiceHandler: JetAudioServiceHandler,
    private val listMusicUseCase: ListMusicUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var duration by savedStateHandle.saveable { mutableStateOf(0L) }
    var progress by savedStateHandle.saveable{ mutableStateOf(0f) }
    var progressString by savedStateHandle.saveable{ mutableStateOf("00:00") }
    var isPlaying by savedStateHandle.saveable{ mutableStateOf(false) }
    var currentSelectedAudio by savedStateHandle.saveable { mutableStateOf(audioDummy) }
    var audioList by savedStateHandle.saveable { mutableStateOf(listOf<Audio>()) }



    init {
        loadAudioData()
    }

    init {
        viewModelScope.launch {
            audioServiceHandler.audioState.collectLatest {mediaState ->
                when(mediaState){
                    is JetAudioState.Buffering -> calculateProgressValue(mediaState.progress)
                    is JetAudioState.CurrentPlaying -> {
                        setPlayingTrack(mediaState.mediaItemIndex)
                    }
                    JetAudioState.Initial -> {}
                    is JetAudioState.Playing -> {
                        isPlaying = mediaState.isPlaying
                        setPlayingTrack(mediaState.mediaItemIndex)
                    }
                    is JetAudioState.Progress -> calculateProgressValue(mediaState.progress)
                    is JetAudioState.Ready -> {
                        duration = mediaState.duration
                    }
                }
            }
        }
    }

    private fun loadAudioData(){
        viewModelScope.launch(Dispatchers.IO) {
            val audio = listMusicUseCase.invoke()
            withContext(Dispatchers.Main){
                audioList = audio
                setMediaItems()
            }
        }
    }

    private fun setPlayingTrack(mediaItemIndex:Int?){
        currentSelectedAudio = audioList[mediaItemIndex?:0]
        audioList = audioList.map {
            it.isSelectedTrack = it.id == currentSelectedAudio.id
            it
        }
    }

    private fun setMediaItems(){
        audioList.map { audio ->
            MediaItem.Builder()
                .setUri(audio.uri)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setAlbumArtist(audio.artist)
                        .setDisplayTitle(audio.title)
                        .setSubtitle(audio.displayName)
                        .setArtworkUri(audio.data.toUri())
                        .build()
                )
                .build()
        }.also {
            audioServiceHandler.setMediaItemList(it)
        }
    }

    private fun calculateProgressValue(currentProgress:Long){
        progress = if(currentProgress > 0) ((currentProgress.toFloat()) / duration.toFloat()) * 100f
        else 0f
        progressString = MusicFunctions.formatDurationMilisecondsToMinSec(currentProgress)
    }


    override fun onCleared() {
        viewModelScope.launch {
            audioServiceHandler.onPlayerEvents(PlayerEvent.Stop)
        }
        super.onCleared()
    }

    fun onUiEvents(uiEvents: UIEvents) = viewModelScope.launch {
        when(uiEvents){
            UIEvents.Backward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Backward)
            UIEvents.Forward -> audioServiceHandler.onPlayerEvents(PlayerEvent.Forward)
            UIEvents.PlayPause -> {
                audioServiceHandler.onPlayerEvents(PlayerEvent.PlayPause)
            }
            is UIEvents.SeekTo -> {
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.SeekTo,
                    seekPosition = ((duration * uiEvents.position) / 100f).toLong()
                )
            }
            UIEvents.SeekToNext -> audioServiceHandler.onPlayerEvents(PlayerEvent.SeekToNext)
            is UIEvents.SelectedAudioChange -> {
                setPlayingTrack(uiEvents.index)
                audioServiceHandler.onPlayerEvents(
                    PlayerEvent.SelectedAudioChange,
                    selectedAudioIndex = uiEvents.index
                )
            }
            is UIEvents.UpdateProgress -> {
                    audioServiceHandler.onPlayerEvents(
                        PlayerEvent.UpdateProgress(uiEvents.newProgress)
                    )
                progress = uiEvents.newProgress
            }
            UIEvents.SeekToBack -> {
                audioServiceHandler.onPlayerEvents(PlayerEvent.SeekToBack)
            }
        }
    }

}

