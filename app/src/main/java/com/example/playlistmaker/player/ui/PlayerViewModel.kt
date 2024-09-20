package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Constants
import com.example.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.library.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerState
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    val playlists: LiveData<List<Playlist>> = playlistInteractor.getPlaylists().asLiveData()

    private val _stateLiveData = MutableLiveData<MediaPlayerState>()
    val stateLiveData: LiveData<MediaPlayerState> = _stateLiveData

    private val _timerLiveData = MutableLiveData<Int>()
    val timerLiveData: LiveData<Int> = _timerLiveData
    private var isTimerRunning = false

    private val _isFavoriteLiveData = MutableLiveData<Boolean>()
    val isFavoriteLiveData: LiveData<Boolean> = _isFavoriteLiveData

    private var placeholderTrack: Track = Track("", "", "", "", 0, "", "", "", "", "")
    private var track = placeholderTrack

    init {
        _stateLiveData.value = MediaPlayerState.PREPARED
        _timerLiveData.value = 0

        viewModelScope.launch(Dispatchers.IO) {
            favoritesInteractor.getFavoritesIDList().collect { list ->
                _isFavoriteLiveData.postValue(list.contains(track.trackId))
            }
        }
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

    fun preparePlayer(loadedTrack: Track) {
        track = loadedTrack
        mediaPlayerInteractor.preparePlayer(track.previewUrl ?: "")
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

    fun addToFavorites() {
        viewModelScope.launch(Dispatchers.IO)
        { favoritesInteractor.addTrackToFavorites(track) }
    }

    fun removeFromFavorites() {
        viewModelScope.launch(Dispatchers.IO)
        { favoritesInteractor.deleteTrackFromFavorites(track) }
    }

    fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch(Dispatchers.IO)
        { playlistInteractor.addTrackToPlaylist(playlist, track) }
    }

}


