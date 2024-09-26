package com.example.playlistmaker.library.domain

import com.example.playlistmaker.library.domain.playlist.Playlist

interface PlaylistClickCallback {
    fun onClickCallback (playlist: Playlist)
    fun onLongClickCallback (playlist: Playlist)
}
