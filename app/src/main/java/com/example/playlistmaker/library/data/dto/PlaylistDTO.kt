package com.example.playlistmaker.library.data.dto

data class PlaylistDTO(
    val playlistId: Long,
    val playlistTitle: String,
    val playlistDescriptor: String,
    val coverImagePath: String,
    var trackList: List<Long>,
    var trackCount: Int,
    val entryTime: Long
)
