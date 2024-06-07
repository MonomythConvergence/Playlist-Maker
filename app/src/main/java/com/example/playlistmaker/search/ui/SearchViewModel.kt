package com.example.playlistmaker.search.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.data.datamodels.recentTracksList
import com.example.playlistmaker.search.domain.SearchRepository

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {


    val state = MutableLiveData<SearchState>()

    init {
        if (recentTracksList.isEmpty())
        {state.value = SearchState.NO_HISTORY}
        else {state.value = SearchState.SHOW_HISTORY}
    }

    fun isRecentListEmpty(): Boolean {
        return searchRepository.isRecentListEmpty()
    }

    fun clearRecentList() {
        searchRepository.clearRecentList()
    }
    fun clearTrackList() {
        searchRepository.clearRecentList()
    }

    fun provideTrackList(): ArrayList<Track> {
        return searchRepository.provideTrackList()
    }

    fun provideRecentTrackList(): ArrayList<Track> {
        return searchRepository.provideRecentTrackList()
    }
}
