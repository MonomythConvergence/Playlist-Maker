package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Constants
import com.example.playlistmaker.library.domain.FavoritesInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerState
import com.example.playlistmaker.search.data.datamodels.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel() {


    private val _stateLiveData = MutableLiveData<MediaPlayerState>()
    val stateLiveData: LiveData<MediaPlayerState> = _stateLiveData

    private val _timerLiveData = MutableLiveData<Int>()
    val timerLiveData: LiveData<Int> = _timerLiveData
    private var isTimerRunning = false

    private val _isFavoriteLiveData = MutableLiveData<Boolean>()
    val isFavoriteLiveData: LiveData<Boolean> = _isFavoriteLiveData

    init {
        _stateLiveData.value = MediaPlayerState.PREPARED
        _timerLiveData.value = 0

    }

    fun updateState() {
        _stateLiveData.value = mediaPlayerInteractor.getPlayerState()
    }

    fun playbackControl() {
        updateState()
        mediaPlayerInteractor.playbackControl()
        timerControl()

    }

    private fun timerControl() {
        isTimerRunning = !isTimerRunning
        if (isTimerRunning) {
            viewModelScope.launch(Dispatchers.Main) {
                while (isTimerRunning) {
                    getCurrentPosition()
                    if (!getIsPlaying()) {
                        _stateLiveData.value = MediaPlayerState.PREPARED
                        mediaPlayerInteractor.setPlayerState(MediaPlayerState.PREPARED)
                        _timerLiveData.value = 0
                        isTimerRunning = false

                    } else {
                        delay(Constants.TIMER_REFRESH)
                    }
                }
            }
        }
    }

    fun getIsPlaying(): Boolean {
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

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        timerControl()
    }

    fun updateFavoriteStatus(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesInteractor.getFavoritesIDList().first().let { list ->
                _isFavoriteLiveData.postValue(list.contains(track.trackId))
            }

        }
    }

    fun addToFavorites(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            favoritesInteractor.addTrackToFavorites(track)
        }
        updateFavoriteStatus(track)

    }

    fun removeFromFavorites(track: Track) {
        viewModelScope.launch(Dispatchers.IO)
        { favoritesInteractor.deleteTrackFromFavorites(track) }
        updateFavoriteStatus(track)
    }
}