package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.domain.SearchInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    val recentTrackListLiveData = MutableLiveData<ArrayList<Track>>()

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> = _state

    private var clickDebounceState: Boolean = false


    init {
        recentTrackListLiveData.value = provideRecentTrackList()

        if (searchInteractor.isRecentListEmpty()) {
            _state.value = SearchState.NO_HISTORY
        } else {
            _state.value = SearchState.SHOW_HISTORY
        }
    }


    fun handleSearch(query: String) {

        viewModelScope.launch(Dispatchers.IO) {
            searchInteractor.searchITunes(query).collect { state ->
                when (state) {
                    is SearchState.LOADING -> {
                        _state.postValue(SearchState.LOADING)
                    }

                    is SearchState.NO_RESULTS -> {
                        _state.postValue(SearchState.NO_RESULTS)
                    }

                    is SearchState.SHOW_RESULTS -> {
                        _state.postValue(SearchState.SHOW_RESULTS)
                    }

                    is SearchState.NETWORK_ERROR -> {
                        _state.postValue(SearchState.NETWORK_ERROR)
                    }

                    else -> {}
                }
            }
        }
    }

    fun setState(newState: SearchState) {
        _state.value = newState
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

    private fun updateRecentList() {
        recentTrackListLiveData.value = searchInteractor.provideRecentTrackList()
    }

    fun setClickDebounce(state: Boolean) {
        clickDebounceState = state
    }

    fun getClickDebounceState(): Boolean {
        return clickDebounceState
    }

}
