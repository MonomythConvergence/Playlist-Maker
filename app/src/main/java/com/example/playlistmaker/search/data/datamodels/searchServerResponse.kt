package com.example.playlistmaker.search.data.datamodels

import com.example.playlistmaker.search.data.datamodels.UnparsedTrack

data class searchServerResonse(
    val resultCount:Int,
    val results: List<UnparsedTrack>
)