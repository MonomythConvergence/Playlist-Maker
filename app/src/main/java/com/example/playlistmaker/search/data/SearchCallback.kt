package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.ui.SearchState

interface SearchCallback { //todo delete after review
    fun onSearchCompleted(searchState: SearchState)
}
