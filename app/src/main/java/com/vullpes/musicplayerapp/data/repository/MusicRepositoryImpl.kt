package com.vullpes.musicplayerapp.data.repository

import com.vullpes.musicplayerapp.domain.model.Audio
import com.vullpes.musicplayerapp.data.dto.toAudio
import com.vullpes.musicplayerapp.data.remote.EndpointDataService
import com.vullpes.musicplayerapp.domain.MusicRepository
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(private val remoteAPI: EndpointDataService) : MusicRepository{



    override suspend fun downloadCatalog(): List<Audio> {
        return try {
            val response = remoteAPI.getMusicCatalog()
            if (response.isSuccessful) {
                 response.body()?.music?.let { soundTrackList ->
                    soundTrackList.map { soundTrack -> soundTrack.toAudio() }
                } ?: emptyList()
            } else emptyList()
        }catch (e:Exception){
            return emptyList()
        }

    }

}