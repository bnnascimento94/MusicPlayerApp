package com.vullpes.musicplayerapp.data.dto

import com.vullpes.musicplayerapp.domain.model.Audio

data class SoundTrack(
    val id: String,
    val title:String,
    val album:String,
    val artist:String,
    val genre:String,
    val source:String,
    val image:String,
    val trackNumber: Int,
    val totalTrackCount: Int,
    val duration: Int,
    val site:String

)

fun SoundTrack.toAudio() = Audio(
    id = (0..100).random().toLong(),
    uri = source,
    displayName = album,
    artist = artist,
    data = image,
    duration = duration,
    title = title,

)