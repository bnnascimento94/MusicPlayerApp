package com.vullpes.musicplayerapp.ui.audio

import androidx.media3.common.MediaItem
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.saveable
import androidx.media3.common.MediaMetadata
import com.vullpes.musicplayerapp.data.local.model.Audio
import com.vullpes.musicplayerapp.data.player.service.JetAudioServiceHandler
import com.vullpes.musicplayerapp.data.player.service.JetAudioState
import com.vullpes.musicplayerapp.data.player.service.PlayerEvent
import com.vullpes.musicplayerapp.data.repository.MusicCatalogRepository
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
    0L,"","","","",0,""

)

@HiltViewModel
class AudioViewModel @Inject constructor(
    private val audioServiceHandler: JetAudioServiceHandler,
    private val repository: MusicCatalogRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var duration by savedStateHandle.saveable { mutableStateOf(0L) }
    var progress by savedStateHandle.saveable{ mutableStateOf(0f) }
    var progressString by savedStateHandle.saveable{ mutableStateOf("00:00") }
    var isPlaying by savedStateHandle.saveable{ mutableStateOf(false) }
    var currentSelectedAudio by savedStateHandle.saveable { mutableStateOf(audioDummy) }
    var audioList by savedStateHandle.saveable { mutableStateOf(listOf<Audio>()) }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState:StateFlow<UiState> = _uiState.asStateFlow()

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
                    JetAudioState.Initial -> _uiState.value = UiState.Initial
                    is JetAudioState.Playing -> {
                        isPlaying = mediaState.isPlaying
                        setPlayingTrack(mediaState.mediaItemIndex)
                    }
                    is JetAudioState.Progress -> calculateProgressValue(mediaState.progress)
                    is JetAudioState.Ready -> {
                        duration = mediaState.duration
                        _uiState.value = UiState.Ready
                    }
                }
            }
        }
    }

    private fun loadAudioData(){
        viewModelScope.launch(Dispatchers.IO) {
            val audio = repository.getAudioCataLog()
            audioList = audio
            withContext(Dispatchers.Main){
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
        progressString = formatDuration(currentProgress)
    }

    private fun formatDuration(duration:Long):String{
        val minutes: Long = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds: Long = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)
                - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
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

