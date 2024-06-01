package com.example.playlistmaker.player.domain

sealed class MediaPlayerState {
    object IDLE : MediaPlayerState() {
        override fun toString() = "IDLE"
    }

    object INITIALIZED : MediaPlayerState() {
        override fun toString() = "INITIALIZED"
    }

    object PREPARED : MediaPlayerState() {
        override fun toString() = "PREPARED"
    }

    object STARTED : MediaPlayerState() {
        override fun toString() = "STARTED"
    }

    object PAUSED : MediaPlayerState() {
        override fun toString() = "PAUSED"
    }

    object STOPPED : MediaPlayerState() {
        override fun toString() = "STOPPED"
    }

    object PLAYBACK_COMPLETED : MediaPlayerState() {
        override fun toString() = "PLAYBACK_COMPLETED"
    }

    object ERROR : MediaPlayerState() {
        override fun toString() = "ERROR"
    }
}


