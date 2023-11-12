package com.vullpes.musicplayerapp.util

import java.util.concurrent.TimeUnit

object MusicFunctions {

    fun formatDurationMilisecondsToMinSec(duration:Long):String{
        val minutes: Long = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
        val seconds: Long = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)
                - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun timeStampToDuration(duration:Long):String{
        val minutes: Long = TimeUnit.MINUTES.convert(duration, TimeUnit.SECONDS)
        val seconds: Long = (duration
                - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
        return String.format("%02d:%02d", minutes, seconds)
    }

}