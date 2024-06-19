package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.data.SearchCallback
import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.domain.SearchRepository

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {

    val recentTrackListLiveData = MutableLiveData<ArrayList<Track>>()

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> = _state

    init {
        recentTrackListLiveData.value = provideRecentTrackList()

        if (searchRepository.isRecentListEmpty()) {
            _state.value = SearchState.NO_HISTORY
        } else {
            _state.value = SearchState.SHOW_HISTORY
        }

    }

    fun handleSearch(query: String){
        (searchRepository.searchITunes (query,object : SearchCallback {
            override fun onSearchCompleted(searchState: SearchState) {
                setState(searchState)
            }
        }))
    }
    fun setState(newState: SearchState) {
        _state.value=newState
    }

    fun isRecentListEmpty(): Boolean {
        return searchRepository.isRecentListEmpty()
    }

    fun clearRecentList() {
        searchRepository.clearRecentList()
        updateRecentList()
    }


    fun provideTrackList(): ArrayList<Track> {
        return searchRepository.provideTrackList()
    }

    fun provideRecentTrackList(): ArrayList<Track> {
        return searchRepository.provideRecentTrackList()
    }


    fun addTrackToRecent(newTrack: Track) {
        searchRepository.addTrackToRecent(newTrack)
        updateRecentList()
    }

    fun encodeRecentTrackList() {
        searchRepository.encodeRecentTrackList()
    }

    private fun updateRecentList(){
        recentTrackListLiveData.value= searchRepository.provideRecentTrackList()
    }


}
