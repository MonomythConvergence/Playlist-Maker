package com.example.playlistmaker.search.domain

import android.content.Context
import com.example.playlistmaker.search.data.SearchCallback
import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.ui.SearchState

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
    fun searchITunes(query : String,callback: SearchCallback)

}
