package com.example.playlistmaker.player.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.player.domain.MediaPlayerInteractor

class PlayerViewModelFactory(private val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            return PlayerViewModel(mediaPlayerInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}