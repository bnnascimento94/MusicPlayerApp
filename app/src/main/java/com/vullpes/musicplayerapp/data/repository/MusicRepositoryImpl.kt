package com.vullpes.musicplayerapp.data.repository

import com.vullpes.musicplayerapp.data.local.ContentResolverHelper
import com.vullpes.musicplayerapp.domain.MusicRepository
import com.vullpes.musicplayerapp.domain.model.Audio
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(private val contentResolver: ContentResolverHelper) : MusicRepository{



    override suspend fun downloadCatalog(): List<Audio> {
        return try {
            contentResolver.getAudioData()
        }catch (e:Exception){
            return emptyList()
        }

    }

}