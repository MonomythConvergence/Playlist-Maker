package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.datamodels.unparsedTrack

data class searchServerResonse(
    val resultCount:Int,
    val results: List<unparsedTrack>
)