package com.example.playlistmaker.player.domain

enum class MediaPlayerState {
    IDLE,
    INITIALIZED,
    PREPARED,
    STARTED,
    PAUSED,
    STOPPED,
    PLAYBACK_COMPLETED,
    ERROR
}
