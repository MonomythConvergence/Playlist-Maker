package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.SearchCallback
import com.example.playlistmaker.search.data.SearchRepository
import com.example.playlistmaker.search.data.datamodels.Track


class SearchInteractorImpl (private val searchRepository: SearchRepository): SearchInteractor {

    override fun searchITunes(query: String, callback: SearchCallback) {
        searchRepository.searchITunes(query, callback)
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