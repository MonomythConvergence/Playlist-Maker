package com.example.playlistmaker.library.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(private val interactor: FavoritesInteractor) : ViewModel() {

    private val _favoritesList = MutableLiveData<List<Track>>()
    val favoritesList: LiveData<List<Track>> = _favoritesList

    private var clickDebounceState: Boolean = false




    fun loadFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getFavorites().collect { list -> _favoritesList.postValue(list) }
        }
    }


    fun setClickDebounce(state: Boolean) {
        clickDebounceState = state
    }

    fun getClickDebounceState(): Boolean {
        return clickDebounceState
    }
}

