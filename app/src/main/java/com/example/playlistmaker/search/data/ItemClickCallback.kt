package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.Track

interface ItemClickCallback {
    fun onClickCallback (track: Track)
}
