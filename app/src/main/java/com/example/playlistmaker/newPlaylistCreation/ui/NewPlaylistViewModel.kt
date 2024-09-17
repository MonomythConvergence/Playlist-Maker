package com.example.playlistmaker.newPlaylistCreation.ui


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.newPlaylistCreation.domain.NewPlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val newPlaylistInteractor: NewPlaylistInteractor,
) : ViewModel() {

    fun addNewPlaylist(name: String, description: String?, path: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            newPlaylistInteractor.addNewPlaylist(name,description,path)
        }
    }

    fun saveAlbumCover(uri: Uri, fileName : String): String {
        return newPlaylistInteractor.saveAlbumCover(uri, fileName)
    }
}