package com.example.playlistmaker.library.domain.playlist

import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getPlaylists() : Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playList: Playlist, track: Track)
    abstract fun getPlaylistByID(playlistID: Long): Flow<Playlist?>
}