package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.ui.SearchState

interface SearchCallback {
    fun onSearchCompleted(searchState: SearchState)
}
