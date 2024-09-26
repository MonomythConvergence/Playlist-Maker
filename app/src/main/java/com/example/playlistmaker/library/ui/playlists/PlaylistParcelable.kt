package com.example.playlistmaker.library.ui.playlists

import android.os.Parcelable
import com.example.playlistmaker.search.ui.TrackParcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaylistParcelable(
    val playlistId: Long,
    var playlistTitle: String,
    var playlistDescriptor: String,
    var coverImagePath: String,
    var trackIDlist : List<Long>,
    var trackList: List<TrackParcelable>,
    var trackCount: Int,
    val entryTime: Long = -1
) : Parcelable