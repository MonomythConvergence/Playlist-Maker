package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerListener
import com.example.playlistmaker.player.domain.MediaPlayerState

class PlayerViewModel(val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel(),
    MediaPlayerListener {
    init {
        mediaPlayerInteractor.setListener(this)
        mediaPlayerInteractor.preparePlayer()
    }

    private var _playerPreparedEvent = MutableLiveData<Boolean>()
    val playerPreparedEvent: LiveData<Boolean> = _playerPreparedEvent

    private var _playerCompletedEvent = MutableLiveData<Boolean>()
    val playerCompletedEvent: LiveData<Boolean> = _playerCompletedEvent

    val isPlaying: Boolean
        get() = mediaPlayerInteractor.getIsPlaying()

    val currentPosition: Int
        get() = mediaPlayerInteractor.getCurrentPosition()

    val duration: Int
        get() = mediaPlayerInteractor.getDuration()

    fun playbackControl() {
        mediaPlayerInteractor.playbackControl()
    }

    override fun onPlayerPrepared() {
        _playerPreparedEvent.value = true
        mediaPlayerInteractor.setPlayerState(MediaPlayerState.PREPARED)

    }

    override fun onPlayerCompleted() {
        mediaPlayerInteractor.setPlayerState(MediaPlayerState.PREPARED)
       _playerCompletedEvent.value = true
    }
}