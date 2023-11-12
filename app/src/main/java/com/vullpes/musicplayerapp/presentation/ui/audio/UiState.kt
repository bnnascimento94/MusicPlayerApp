package com.vullpes.musicplayerapp.presentation.ui.audio

sealed class UiState{
    object Initial: UiState()
    object Ready: UiState()
}
