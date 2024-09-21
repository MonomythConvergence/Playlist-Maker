package com.example.playlistmaker.library.domain.playlist

import com.example.playlistmaker.search.domain.Track
//todo check imports from android. in other domain classes and refactor
data class Playlist(
    val playlistId: Long,
    var playlistTitle: String,
    var playlistDescriptor: String,
    var coverImagePath: String,
    var trackIDlist : List<Long>,
    var trackList: List<Track>,
    var trackCount: Int,
    val entryTime: Long = -1
)