package com.example.playlistmaker.search.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class TrackParcelable(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val trackId: Long,
    val collectionName : String?,
    val releaseDate : String,
    val primaryGenreName : String,
    val country : String,
    val previewUrl : String?
) : Parcelable