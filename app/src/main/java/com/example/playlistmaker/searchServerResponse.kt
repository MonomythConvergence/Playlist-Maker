package com.example.playlistmaker

import com.example.playlistmaker.audioplayer.data.dto.unparsedTrack

data class searchServerResonse(
    val resultCount:Int,
    val results: List<unparsedTrack>
)