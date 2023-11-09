package com.vullpes.musicplayerapp.data.local.model

import android.net.Uri

data class Audio(
    val id: Long,
    val uri: Uri,
    val displayName:String,
    val artist:String,
    val data:String,
    val duration: Int,
    val title:String
)
