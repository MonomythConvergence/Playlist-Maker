package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.SearchCallback
import com.example.playlistmaker.search.data.datamodels.Track

interface SearchInteractor {
    fun isRecentListEmpty():Boolean
    fun clearRecentList()
    fun provideTrackList(): ArrayList<Track>
    fun provideRecentTrackList(): ArrayList<Track>
    fun addTrackToRecent(newTrack: Track)
    fun encodeRecentTrackList()
    fun searchITunes(query : String, callback : SearchCallback)
}