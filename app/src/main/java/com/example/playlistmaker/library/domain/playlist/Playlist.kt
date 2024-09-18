package com.example.playlistmaker.library.domain.playlist

data class Playlist(
    val playlistId: Long,
    val playlistTitle: String,
    val playlistDescriptor: String,
    val coverImagePath: String,
    var trackList: List<Long>,
    var trackCount: Int,
    val entryTime: Long = 0
)