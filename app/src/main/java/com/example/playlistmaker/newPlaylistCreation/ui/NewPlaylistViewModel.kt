package com.example.playlistmaker.newPlaylistCreation.ui


import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.editPlaylist.domain.ConvertBitmapInteractor
import com.example.playlistmaker.editPlaylist.domain.PathToBitmapConverterCallback
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.library.ui.playlists.PlaylistMapper
import com.example.playlistmaker.library.ui.playlists.PlaylistParcelable
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.ui.TrackMapper
import com.example.playlistmaker.search.ui.TrackParcelable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val convertBitmapInteractor: ConvertBitmapInteractor,
    private val trackMapper: TrackMapper,
    private val playlistMapper: PlaylistMapper
) : ViewModel() {

    fun addNewPlaylist(name: String, description: String?, path: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.addNewPlaylist(name, description, path)
        }
    }

    fun updatePlaylist(playlist: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.updatePlaylist(playlist)
        }
    }

    fun saveAlbumCover(uri: Uri, fileName: String): String {
        return playlistInteractor.saveAlbumCover(uri.toString(), fileName)
    }

    fun getBitmap(path: String, pathToBitmapConverterCallback: PathToBitmapConverterCallback) {
        viewModelScope.launch(Dispatchers.IO) {
            convertBitmapInteractor.convertPathToBitmap(path, pathToBitmapConverterCallback)
        }
    }

    fun mapTrackToParcelable(track: Track?): Parcelable? {
        return if (track != null) {
            trackMapper.toParcelable(track)
        } else {
            null
        }
    }


    fun mapParcelableToTrack(parcelable: Parcelable?): Track? {
        return if (parcelable != null) {
            trackMapper.toDomain(parcelable as TrackParcelable)
        } else {
            null
        }
    }

    fun mapPlaylistToParcelable(playlist: Playlist?): Parcelable? {
        return if (playlist != null) {
            playlistMapper.toParcelable(playlist)
        } else {
            null
        }
    }

    fun mapParcelableToPlaylist(parcelable: Parcelable?): Playlist? {
        return if (parcelable != null) {
            playlistMapper.toDomain(parcelable as PlaylistParcelable)
        } else {
            null
        }
    }

}