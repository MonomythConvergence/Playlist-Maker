package com.example.playlistmaker.player.ui

import com.example.playlistmaker.library.data.dto.PlaylistDTO

data class PlaylistWithTrackFlag(
    val playlist: PlaylistDTO,
    var containsTrack: Boolean
)