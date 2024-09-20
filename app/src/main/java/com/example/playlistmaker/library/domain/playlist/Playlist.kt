package com.example.playlistmaker.library.domain.playlist

import android.os.Parcelable
import com.example.playlistmaker.search.domain.Track
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val playlistId: Long,
    var playlistTitle: String,
    var playlistDescriptor: String,
    var coverImagePath: String,
    var trackIDlist : List<Long>,
    var trackList: List<Track>,
    var trackCount: Int,
    val entryTime: Long = -1
): Parcelable