package com.example.playlistmaker.search.ui

sealed class SearchState  {
    object SHOW_HISTORY: SearchState()

    object LOADING: SearchState()

    object SHOW_RESULTS: SearchState()

    object NETWORK_ERROR: SearchState()

    object NO_RESULTS: SearchState()

    object NO_HISTORY: SearchState()
}