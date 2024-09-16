package com.example.playlistmaker.library.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.playlistmaker.library.data.dto.PlaylistDTO
import com.example.playlistmaker.library.domain.playlist.PlaylistInteractor

class PlaylistsFragmentViewModel(val playlistInteractor: PlaylistInteractor) : ViewModel() {

    val playlists: LiveData<List<PlaylistDTO>> = playlistInteractor.getPlaylists().asLiveData()


}