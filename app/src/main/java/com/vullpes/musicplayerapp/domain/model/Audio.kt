package com.vullpes.musicplayerapp.domain.model

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import java.io.Serializable

data class Audio(
    val id: String,
    val uri: String,
    val displayName:String,
    val artist:String,
    val data:String,
    val duration: Int,
    val title:String,
    var isSelectedTrack:Boolean = false
): Serializable


