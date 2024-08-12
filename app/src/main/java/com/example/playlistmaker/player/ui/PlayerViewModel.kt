package com.example.playlistmaker.player.ui

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class PlayerViewModel(val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {


    private val _stateLiveData = MutableLiveData<MediaPlayerState>()
    val stateLiveData: LiveData<MediaPlayerState> = _stateLiveData

    private val _timerLiveData = MutableLiveData<Int>()
    val timerLiveData: LiveData<Int> = _timerLiveData
    var isTimerRunning = false

    init {
        _stateLiveData.value = MediaPlayerState.PREPARED
        _timerLiveData.value = 0
    }

    fun updateState() {
        _stateLiveData.value = mediaPlayerInteractor.getPlayerState()
    }

    fun playbackControl() {
        mediaPlayerInteractor.playbackControl()
        timerControl()
    }

    private fun timerControl() {
        isTimerRunning = !isTimerRunning
        if (isTimerRunning) {
            viewModelScope.launch(Dispatchers.Main) {
                while (isTimerRunning) {
                    getCurrentPosition()
                    Log.d("MyTag", "position updated")
                    if (!getIsPlaying() && isPlaybackFinished()) {
                        Log.d("MyTag", "should stop")
                        _stateLiveData.value = MediaPlayerState.PREPARED
                        _timerLiveData.value = 0
                        isTimerRunning = false
                    } else {
                        delay(300)
                    }

                }


            }
        }
    }

    private fun getIsPlaying(): Boolean {
        return mediaPlayerInteractor.getIsPlaying()
    }

    fun preparePlayer(url: String) {
        mediaPlayerInteractor.preparePlayer(url)
    }

    fun releasePlayer() {
        mediaPlayerInteractor.releasePlayer()
    }

    private fun getCurrentPosition() {
        _timerLiveData.value = mediaPlayerInteractor.getCurrentPosition()
    }

    fun setPlayerState(state: MediaPlayerState) {
        mediaPlayerInteractor.setPlayerState(state)
    }


    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        timerControl()
    }

    fun isPlaybackFinished() : Boolean{
        return (mediaPlayerInteractor.getCurrentPosition()>=mediaPlayerInteractor.getDuration())
    }
}