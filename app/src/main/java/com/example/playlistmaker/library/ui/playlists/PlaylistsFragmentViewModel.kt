package com.example.playlistmaker.library.ui.playlists

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlist.PlaylistInteractor

class PlaylistsFragmentViewModel(
    playlistInteractor: PlaylistInteractor,
    private val playlistMapper: PlaylistMapper
) : ViewModel() {

    val playlists: LiveData<List<Playlist>> = playlistInteractor.getPlaylists().asLiveData()

    fun mapPlaylistToParcelable(playlist: Playlist?): Parcelable? {
        return if (playlist != null) {
            playlistMapper.toParcelable(playlist)
        } else {
            null
        }
    }


}