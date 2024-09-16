package com.example.playlistmaker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0,
    val playlistTitle: String,
    val playlistDescriptor: String,
    val coverImagePath: String,
    var trackList: List<Long>,
    var trackCount: Int,
    val entryTime: Long = Date().time
)