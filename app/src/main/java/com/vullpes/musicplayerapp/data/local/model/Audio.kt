package com.vullpes.musicplayerapp.data.local.model

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import java.io.Serializable

data class Audio(
    val id: Long,
    val uri: String,
    val displayName:String,
    val artist:String,
    val data:String,
    val duration: Int,
    val title:String
): Serializable



fun Audio.toMediaItem() = MediaItem.Builder()
    .setUri(uri)
    .setMediaMetadata(
        MediaMetadata.Builder()
            .setAlbumArtist(artist)
            .setDisplayTitle(title)
            .setSubtitle(displayName)
            .build()
    )
    .build()