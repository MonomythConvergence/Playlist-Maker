package com.example.playlistmaker.search.data.datamodels

data class searchServerResonse(
    val resultCount:Int,
    val results: List<UnparsedTrack>
)