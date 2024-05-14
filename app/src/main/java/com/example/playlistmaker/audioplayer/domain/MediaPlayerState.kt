package com.example.playlistmaker.audioplayer.domain

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
