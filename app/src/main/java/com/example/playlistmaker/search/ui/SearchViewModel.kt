package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.data.SearchCallback
import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.domain.SearchInteractor

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    val recentTrackListLiveData = MutableLiveData<ArrayList<Track>>()

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> = _state

    val searchCallback = object : SearchCallback {
        override fun onSearchCompleted(searchState: SearchState) {
            setState(searchState)
        }}

    init {
        recentTrackListLiveData.value = provideRecentTrackList()

        if (searchInteractor.isRecentListEmpty()) {
            _state.value = SearchState.NO_HISTORY
        } else {
            _state.value = SearchState.SHOW_HISTORY
        }
    }




    fun handleSearch(query: String){
        searchInteractor.searchITunes(query,searchCallback)
    }
    fun setState(newState: SearchState) {
        _state.value=newState
    }

    fun isRecentListEmpty(): Boolean {
        return searchInteractor.isRecentListEmpty()
    }

    fun clearRecentList() {
        searchInteractor.clearRecentList()
        updateRecentList()
    }


    fun provideTrackList(): ArrayList<Track> {
        return searchInteractor.provideTrackList()
    }

    fun provideRecentTrackList(): ArrayList<Track> {
        return searchInteractor.provideRecentTrackList()
    }


    fun addTrackToRecent(newTrack: Track) {
        searchInteractor.addTrackToRecent(newTrack)
        updateRecentList()
    }

    fun encodeRecentTrackList() {
        searchInteractor.encodeRecentTrackList()
    }

    private fun updateRecentList(){
        recentTrackListLiveData.value= searchInteractor.provideRecentTrackList()
    }


}
