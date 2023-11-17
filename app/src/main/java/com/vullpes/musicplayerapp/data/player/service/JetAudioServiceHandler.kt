package com.vullpes.musicplayerapp.data.player.service

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class JetAudioServiceHandler @Inject constructor(
    private val exoPlayer: ExoPlayer
): Player.Listener {
    private val _audioState: MutableStateFlow<JetAudioState> = MutableStateFlow(JetAudioState.Initial)
    val audioState: StateFlow<JetAudioState> = _audioState.asStateFlow()
    private val progressScope = CoroutineScope(Dispatchers.Main)
    private var job: Job?= null
    init {
        exoPlayer.addListener(this)
        job = Job()
    }

    fun addMediaItem(mediaItem: MediaItem){
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun setMediaItemList(mediaItems:List<MediaItem>){
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }

    suspend fun onPlayerEvents(
        playerEvent: PlayerEvent,
        selectedAudioIndex:Int = -1,
        seekPosition:Long = 0
    ){
        when(playerEvent){
            PlayerEvent.Backward -> exoPlayer.seekBack()
            PlayerEvent.Forward -> exoPlayer.seekForward()
            PlayerEvent.PlayPause -> playOrPause()
            PlayerEvent.SeekTo -> exoPlayer.seekTo(seekPosition)
            PlayerEvent.SeekToNext -> exoPlayer.seekToNext()
            PlayerEvent.SelectedAudioChange -> {
                when(selectedAudioIndex){
                    exoPlayer.currentMediaItemIndex ->{
                        playOrPause()
                    }
                    else ->{
                        exoPlayer.seekToDefaultPosition(selectedAudioIndex)
                        _audioState.value = JetAudioState.Playing(isPlaying = true, mediaItemIndex = exoPlayer.currentMediaItemIndex)
                        exoPlayer.playWhenReady = true
                        startProgressUpdate()
                    }
                }
            }
            PlayerEvent.Stop -> stopProgressUpdate()
            is PlayerEvent.UpdateProgress ->{
                exoPlayer.seekTo(
                    (exoPlayer.duration * playerEvent.newProgress).toLong()
                )
            }

            PlayerEvent.SeekToBack -> exoPlayer.seekToPrevious()
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when(playbackState){
            ExoPlayer.STATE_BUFFERING -> _audioState.value = JetAudioState.Buffering(exoPlayer.currentPosition)
            ExoPlayer.STATE_READY -> _audioState.value = JetAudioState.Ready(exoPlayer.duration)
        }
    }

    override fun onTracksChanged(tracks: Tracks) {
        _audioState.value = JetAudioState.CurrentPlaying(exoPlayer.currentMediaItemIndex)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _audioState.value = JetAudioState.Playing(isPlaying = isPlaying,mediaItemIndex = exoPlayer.currentMediaItemIndex)
        if(isPlaying){
            progressScope.launch {
                startProgressUpdate()
            }
        }else{
            stopProgressUpdate()
        }
    }



    private suspend fun playOrPause(){
        if(exoPlayer.isPlaying){
            exoPlayer.pause()
            stopProgressUpdate()
        }else{
            exoPlayer.play()
            _audioState.value = JetAudioState.Playing(isPlaying = true,mediaItemIndex = exoPlayer.currentMediaItemIndex)
            startProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true){
            delay(500)
            _audioState.value = JetAudioState.Progress(exoPlayer.currentPosition)
        }
    }
    private fun stopProgressUpdate(){
        job?.cancel()
        progressScope.cancel()
        _audioState.value = JetAudioState.Playing(isPlaying = false, mediaItemIndex = exoPlayer.currentMediaItemIndex)
    }
}

sealed class PlayerEvent{
    object PlayPause: PlayerEvent()
    object SelectedAudioChange: PlayerEvent()
    object Backward: PlayerEvent()
    object SeekToNext: PlayerEvent()

    object SeekToBack: PlayerEvent()
    object Forward: PlayerEvent()
    object SeekTo: PlayerEvent()
    object Stop: PlayerEvent()
    data class UpdateProgress(val newProgress:Float): PlayerEvent()
}

sealed class JetAudioState{
    object Initial: JetAudioState()
    data class Ready(val duration:Long): JetAudioState()
    data class Progress(val progress:Long): JetAudioState()
    data class Buffering(val progress: Long): JetAudioState()
    data class Playing(val isPlaying:Boolean, val mediaItemIndex: Int): JetAudioState()
    data class CurrentPlaying(val mediaItemIndex: Int): JetAudioState()
}