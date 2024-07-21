package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.datamodels.Track

interface ItemClickCallback {
    fun onClickCallback (track: Track)
}
