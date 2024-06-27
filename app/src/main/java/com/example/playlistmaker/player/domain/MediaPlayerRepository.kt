package com.example.playlistmaker.player.domain

interface MediaPlayerRepository {
    fun preparePlayer(url: String)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun playbackControl()
    fun getCurrentPosition(): Int
    fun getDuration(): Int
    fun getIsPlaying(): Boolean
    fun setPlayerState(mediaPlayerState : MediaPlayerState)
    fun resetPlayer()

    var mediaPlayerState : MediaPlayerState

}