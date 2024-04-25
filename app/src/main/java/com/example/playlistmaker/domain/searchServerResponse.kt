package com.example.playlistmaker.domain

data class searchServerResonse(
    val resultCount:Int,
    val results: List<unparsedTrack>
)
{
}