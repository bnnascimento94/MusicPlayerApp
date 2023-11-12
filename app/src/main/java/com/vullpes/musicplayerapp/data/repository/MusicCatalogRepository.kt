package com.vullpes.musicplayerapp.data.repository

import com.vullpes.musicplayerapp.data.local.model.Audio
import com.vullpes.musicplayerapp.data.local.model.toAudio
import com.vullpes.musicplayerapp.data.remote.EndpointDataService
import javax.inject.Inject

class MusicCatalogRepository @Inject constructor(private val remoteAPI: EndpointDataService) {

    suspend fun getAudioCataLog(): List<Audio> {
        val response = remoteAPI.getMusicCatalog()
        return if (response.isSuccessful) {
            return response.body()?.music?.let { soundTrackList ->
                soundTrackList.map { soundTrack -> soundTrack.toAudio() }
            } ?: emptyList()
        } else emptyList()
    }

}