package com.example.playlistmaker.player.ui

import com.example.playlistmaker.library.domain.playlist.Playlist

interface AddToPlaylistClickback {
    fun addSelectedTrackToPlaylist(playlist : Playlist, added : Boolean)
}