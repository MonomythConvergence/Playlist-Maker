package com.example.playlistmaker.newPlaylistCreation.ui


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.editPlaylist.domain.ConvertBitmapInteractor
import com.example.playlistmaker.editPlaylist.domain.PathToBitmapConverterCallback
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor, private val convertBitmapInteractor: ConvertBitmapInteractor)
 : ViewModel() {

    fun addNewPlaylist(name: String, description: String?, path: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.addNewPlaylist(name,description,path)
        }
    }

    fun updatePlaylist(playlist: Playlist){
        viewModelScope.launch(Dispatchers.IO) {
        playlistInteractor.updatePlaylist(playlist)}
    }

    fun saveAlbumCover(uri: Uri, fileName : String): String {
        return playlistInteractor.saveAlbumCover(uri.toString(), fileName)
    }

    fun getBitmap(path : String, pathToBitmapConverterCallback: PathToBitmapConverterCallback) {
        viewModelScope.launch(Dispatchers.IO) {
        convertBitmapInteractor.convertPathToBitmap(path, pathToBitmapConverterCallback)
    }
    }

}