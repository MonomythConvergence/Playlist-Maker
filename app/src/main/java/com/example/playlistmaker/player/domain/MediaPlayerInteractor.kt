package com.example.playlistmaker.player.domain

interface MediaPlayerInteractor {
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun playbackControl()
    fun getCurrentPosition(): Int
    fun getDuration(): Int
    fun getIsPlaying(): Boolean
    fun getPlayerState() : MediaPlayerState
    fun setPlayerState(mediaPlayerState : MediaPlayerState)
}