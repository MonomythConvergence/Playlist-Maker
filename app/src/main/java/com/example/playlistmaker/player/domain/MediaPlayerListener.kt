package com.example.playlistmaker.player.domain

interface MediaPlayerListener {
    fun onPlayerPrepared()
    fun onPlayerCompleted()
}
