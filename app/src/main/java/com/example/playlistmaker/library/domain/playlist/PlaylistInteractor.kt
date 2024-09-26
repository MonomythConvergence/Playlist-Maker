package com.example.playlistmaker.library.domain.playlist

import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getPlaylists() : Flow<List<Playlist>>
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    fun getPlaylistByID(playlistID: Long): Flow<Playlist?>
    suspend fun removeTrackFromPlaylist(playlist: Playlist, trackToDelete: Track)
    suspend fun deletePlaylist(playlistToDelete: Playlist)
    fun saveAlbumCover(bitmap: String, fileName: String): String
    suspend fun addNewPlaylist(name: String, description: String?, path: String?)
}