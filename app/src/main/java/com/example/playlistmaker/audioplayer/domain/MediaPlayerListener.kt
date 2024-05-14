package com.example.playlistmaker.audioplayer.domain

interface MediaPlayerListener {
    fun onPlayerPrepared()
    fun onPlayerCompleted()
}
