package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.ui.SearchState
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun isRecentListEmpty():Boolean
    fun clearRecentList()
    fun provideTrackList(): ArrayList<Track>
    fun provideRecentTrackList(): ArrayList<Track>
    fun addTrackToRecent(newTrack: Track)
    fun encodeRecentTrackList()
    fun searchITunes(query : String) : Flow<SearchState>
}