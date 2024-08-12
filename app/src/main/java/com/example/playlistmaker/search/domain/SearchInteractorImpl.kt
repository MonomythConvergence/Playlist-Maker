package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.ui.SearchState
import kotlinx.coroutines.flow.Flow


class SearchInteractorImpl (private val searchRepository: SearchRepository): SearchInteractor {

    override fun searchITunes(query: String): Flow<SearchState> {
        return searchRepository.searchITunesFlow(query)
    }

    override fun isRecentListEmpty(): Boolean {
        return searchRepository.isRecentListEmpty()
    }

    override fun clearRecentList() {
        searchRepository.clearRecentList()

    }


    override fun provideTrackList(): ArrayList<Track> {
        return searchRepository.provideTrackList()
    }

    override fun provideRecentTrackList(): ArrayList<Track> {
        return searchRepository.provideRecentTrackList()
    }


    override fun addTrackToRecent(newTrack: Track) {
        searchRepository.addTrackToRecent(newTrack)

    }

    override fun encodeRecentTrackList() {
        searchRepository.encodeRecentTrackList()
    }


}