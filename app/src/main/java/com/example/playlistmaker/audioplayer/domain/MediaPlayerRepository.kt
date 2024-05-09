package com.example.playlistmaker.audioplayer.domain

import com.example.playlistmaker.audioplayer.data.MediaPlayerState

interface MediaPlayerRepository {
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun playbackControl()
    fun getCurrentPosition(): Int
    fun getDuration(): Int
    fun getIsPlaying(): Boolean
    fun setPlayerState(mediaPlayerState : MediaPlayerState)

    var mediaPlayerState : MediaPlayerState

}