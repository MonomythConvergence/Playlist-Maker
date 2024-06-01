package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.MediaPlayerState

class PlayerViewModel() : ViewModel() {


    private val _stateLiveData = MutableLiveData<MediaPlayerState>()
    val stateLiveData: LiveData<MediaPlayerState> = _stateLiveData

    init {
        _stateLiveData.value = MediaPlayerState.PREPARED
    }


    fun updateState(newState : MediaPlayerState) {
        _stateLiveData.value = newState
    }




}