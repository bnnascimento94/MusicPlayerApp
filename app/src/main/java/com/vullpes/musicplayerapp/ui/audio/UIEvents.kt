package com.vullpes.musicplayerapp.ui.audio

sealed class UIEvents{
    object PlayPause: UIEvents()
    data class SelectedAudioChange(val index:Int): UIEvents()
    data class SeekTo(val position:Float): UIEvents()
    object SeekToNext:UIEvents()
    object Backward: UIEvents()
    object Forward:UIEvents()
    data class UpdateProgress(val newProgress: Float): UIEvents()

}
