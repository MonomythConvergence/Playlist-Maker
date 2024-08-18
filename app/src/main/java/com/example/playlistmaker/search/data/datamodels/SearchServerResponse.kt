package com.example.playlistmaker.search.data.datamodels

data class SearchServerResonse(
    val resultCount:Int,
    val results: List<UnparsedTrack>
)