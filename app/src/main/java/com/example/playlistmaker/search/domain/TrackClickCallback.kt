package com.example.playlistmaker.search.domain

interface TrackClickCallback {
    fun onClickCallback (track: Track)
    fun onLongClickCallback (track: Track)
}
