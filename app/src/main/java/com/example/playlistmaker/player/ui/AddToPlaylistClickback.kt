package com.example.playlistmaker.player.ui

import com.example.playlistmaker.library.data.dto.PlaylistDTO

interface AddToPlaylistClickback {
    abstract fun addSelectedTrackToPlaylist(playlist : PlaylistDTO, added : Boolean)
}