package com.example.playlistmaker.library.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.FavoritesRepository
import com.example.playlistmaker.search.data.datamodels.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(private val repository: FavoritesRepository) : ViewModel() {

    private val _favoritesList = MutableLiveData<List<Track>>()
    val favoritesList: LiveData<List<Track>> = _favoritesList

    private var clickDebounceState: Boolean = false

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavorites().collect { list -> _favoritesList.postValue(list) }
        }
    }

    fun provideFavoritesList(): List<Track> {
        return favoritesList.value!!
    }

    fun setClickDebounce(state: Boolean) {
        clickDebounceState = state
    }

    fun getClickDebounceState(): Boolean {
        return clickDebounceState
    }
}

