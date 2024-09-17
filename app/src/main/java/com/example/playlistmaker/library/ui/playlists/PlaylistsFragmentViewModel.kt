package com.example.playlistmaker.library.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlist.PlaylistInteractor

class PlaylistsFragmentViewModel(playlistInteractor: PlaylistInteractor) : ViewModel() {

    val playlists: LiveData<List<Playlist>> = playlistInteractor.getPlaylists().asLiveData()


}