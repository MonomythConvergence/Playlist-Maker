package com.example.playlistmaker

data class searchServerResonse(
    val resultCount:Int,
    val results: List<UnparsedTrack>
)