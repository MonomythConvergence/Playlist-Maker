package com.example.playlistmaker.editPlaylist.ui

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.example.playlistmaker.sharing.domain.ExternalInteractionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val convertBitmapInteractor: ConvertBitmapInteractor,
    private val externalInteractionHandler: ExternalInteractionHandler,
    private val playlistMapper: PlaylistMapper,
    private val trackMapper: TrackMapper
) : ViewModel() {

    private val _playlistLiveData = MutableLiveData<Playlist>()
    val playlistLiveData: LiveData<Playlist> = _playlistLiveData

    private val _playlistTracksLiveData = MutableLiveData<List<Track>>()
    val playlistTracksLiveData: LiveData<List<Track>> = _playlistTracksLiveData

    init {
        _playlistTracksLiveData.value = emptyList()
        _playlistLiveData.value = Playlist(-1, "", "", "", emptyList(), emptyList(), -1, -1)
    }

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


    fun updatePlaylistLiveData(playlist: Playlist) {
        _playlistLiveData.postValue(playlist)
    }

    fun updatePlaylistTracksLiveData(newList: List<Track>) {
        _playlistTracksLiveData.postValue(newList)
    }

    fun convertPathToBitmap(
        path: String,
        pathToBitmapConverterCallback: PathToBitmapConverterCallback
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            convertBitmapInteractor.convertPathToBitmap(path, pathToBitmapConverterCallback)
        }
    }

    fun addTrackToPlaylist(trackToAdd: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.addTrackToPlaylist(_playlistLiveData.value!!, trackToAdd)
            var updatedList = _playlistLiveData
            val newLocalTrackList = _playlistLiveData.value!!.trackList.toMutableList()
            newLocalTrackList.add(trackToAdd)
            val newLocalIDList = _playlistLiveData.value!!.trackIDlist.toMutableList()
            newLocalIDList.add(trackToAdd.trackId)
            updatedList.value!!.trackList = newLocalTrackList
            updatedList.value!!.trackIDlist = newLocalIDList
            updatedList.value!!.trackCount += 1
            updatePlaylistLiveData(updatedList.value!!)
            updatePlaylistTracksLiveData(newLocalTrackList)
        }
    }

    fun deleteTrackFromPlaylist(trackToDelete: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.removeTrackFromPlaylist(_playlistLiveData.value!!, trackToDelete)
            var updatedList = _playlistLiveData
            val newLocalTrackList = _playlistLiveData.value!!.trackList.toMutableList()
            newLocalTrackList.remove(trackToDelete)
            val newLocalIDList = _playlistLiveData.value!!.trackIDlist.toMutableList()
            newLocalIDList.remove(trackToDelete.trackId)
            updatedList.value!!.trackList = newLocalTrackList
            updatedList.value!!.trackIDlist = newLocalIDList
            updatedList.value!!.trackCount -= 1
            updatePlaylistLiveData(updatedList.value!!)
            updatePlaylistTracksLiveData(newLocalTrackList)
        }
    }

    fun deletePlaylist(playlistToDelete: Playlist) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.deletePlaylist(playlistToDelete)

        }
    }

    fun handleShare(s: String) {
        externalInteractionHandler.openShareMenu(s)
    }


    fun mapParcelableToPlaylist(parcelable: Parcelable?): Playlist? {
        return if (parcelable != null) {
            playlistMapper.toDomain(parcelable as PlaylistParcelable)
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

    fun mapTrackToParcelable(track: Track?): Parcelable? {
        return if (track != null) {
            trackMapper.toParcelable(track)
        } else {
            null
        }
    }


}