package com.vullpes.musicplayerapp.domain

import com.vullpes.musicplayerapp.domain.model.Audio

interface MusicRepository {

    suspend fun downloadCatalog():List<Audio>

}