package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.datamodels.Track

interface SearchRepository {
    fun isRecentListEmpty():Boolean
    fun clearRecentList()
    fun clearTrackList()
    fun provideTrackList(): ArrayList<Track>
    fun provideRecentTrackList(): ArrayList<Track>
    fun addTrackToResults(newTrack: Track)
    fun addTrackToRecent(newTrack: Track)
    fun encodeRecentTrackList()
    fun decodeRecentTrackList()
}
