package com.vullpes.musicplayerapp.ui.audio

sealed class UiState{
    object Initial:UiState()
    object Ready:UiState()
}
