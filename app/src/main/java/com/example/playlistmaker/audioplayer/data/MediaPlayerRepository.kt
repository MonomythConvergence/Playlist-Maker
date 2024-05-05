package com.example.playlistmaker.audioplayer.data

interface MediaPlayerRepository {
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun playbackControl()
    fun getCurrentPosition(): Int
    fun getDuration(): Int
    fun getIsPlaying(): Boolean
}