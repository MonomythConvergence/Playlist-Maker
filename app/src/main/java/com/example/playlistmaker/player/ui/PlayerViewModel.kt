package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerState

class PlayerViewModel(val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {


    private val _stateLiveData = MutableLiveData<MediaPlayerState>()
    val stateLiveData: LiveData<MediaPlayerState> = _stateLiveData

    init {
        _stateLiveData.value = MediaPlayerState.PREPARED
    }

    fun updateState() {
        _stateLiveData.value = mediaPlayerInteractor.getPlayerState()
    }


    fun playbackControl(){
        mediaPlayerInteractor.playbackControl()
    }

    fun getIsPlaying(): Boolean {
        return mediaPlayerInteractor.getIsPlaying()
    }

    fun preparePlayer(url : String) {
        mediaPlayerInteractor.preparePlayer(url)
    }

    fun releasePlayer() {
        mediaPlayerInteractor.releasePlayer()
    }

    fun getCurrentPosition(): Int {
        return mediaPlayerInteractor.getCurrentPosition()
    }

    fun setPlayerState(state : MediaPlayerState) {
        mediaPlayerInteractor.setPlayerState(state)
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
    }



}