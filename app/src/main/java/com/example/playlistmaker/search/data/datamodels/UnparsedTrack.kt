package com.example.playlistmaker.search.data.datamodels

class UnparsedTrack(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val trackId : Long,
    val collectionName : String?,
    val releaseDate : String,
    val primaryGenreName : String,
    val country : String,
    val previewUrl : String
)