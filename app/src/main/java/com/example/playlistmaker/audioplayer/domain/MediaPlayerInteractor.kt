package com.example.playlistmaker.audioplayer.domain

interface MediaPlayerInteractor {
    fun setListener(mediaPlayerListener: MediaPlayerListener)
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun playbackControl()
    fun getCurrentPosition(): Int
    fun getDuration(): Int
    fun getIsPlaying(): Boolean

}