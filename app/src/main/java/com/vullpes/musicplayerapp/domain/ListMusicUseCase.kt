package com.vullpes.musicplayerapp.domain

import com.vullpes.musicplayerapp.domain.model.Audio
import javax.inject.Inject

class ListMusicUseCase @Inject constructor(private val musicRepository: MusicRepository) {

    suspend operator fun invoke() = musicRepository.downloadCatalog()
}