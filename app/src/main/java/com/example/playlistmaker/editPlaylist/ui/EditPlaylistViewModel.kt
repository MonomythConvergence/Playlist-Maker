package com.example.playlistmaker.editPlaylist.ui;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.player.domain.BottomSheetState
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val _bottomSheetLiveData = MutableLiveData<BottomSheetState>()
    val bottomSheetLiveData: LiveData<BottomSheetState> get() = _bottomSheetLiveData

    private val _playlistLiveData = MutableLiveData<Playlist>()
    val playlistLiveData: LiveData<Playlist> = _playlistLiveData

    private val _playlistTracksLiveData = MutableLiveData<List<Track>>()
    val playlistTracksLiveData: LiveData<List<Track>> = _playlistTracksLiveData

    fun updatePlaylistInDB(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.updatePlaylist(playlist)
        }
    }

    fun getPlaylistByID(playlistID: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            playlistInteractor.getPlaylistByID(playlistID).collect { playlist ->
                if (playlist != null) {
                    updatePlaylistLiveData(playlist)
                }
            }
        }
    }

    fun updateBottomSheetState(state: BottomSheetState) {
        _bottomSheetLiveData.value = state
    }

    fun updatePlaylistLiveData(playlist: Playlist) {
        _playlistLiveData.value = playlist
        if (playlist.trackList)
    }

    fun
}