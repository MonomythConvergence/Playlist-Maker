package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.MediaPlayerState

class PlayerViewModel() : ViewModel() {

    lateinit var mediaPlayerInteractor : MediaPlayerInteractor


    private val _stateLiveData = MutableLiveData<MediaPlayerState>()
    val stateLiveData: LiveData<MediaPlayerState> = _stateLiveData

    init {
        _stateLiveData.value = MediaPlayerState.PREPARED
    }


    fun updateState(newState : MediaPlayerState) {
        _stateLiveData.value = newState
    }
    fun initializeMediaPlayerinstances(url: String) {
        val mediaPlayerRepository = MediaPlayerRepositoryImpl(url)
        mediaPlayerInteractor = MediaPlayerInteractorImpl(mediaPlayerRepository)
    }

    fun giveMediaPlayerInteractor(): MediaPlayerInteractor {
        return mediaPlayerInteractor
    }



}