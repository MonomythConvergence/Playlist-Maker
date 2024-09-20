package com.example.playlistmaker.library.domain.playlist


import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    suspend fun updatePlaylist(playlist: Playlist)
    fun getPlaylistByID(playlistID: Long): Flow<Playlist?>
    suspend fun removeTrackFromPlaylist(playlist: Playlist, track: Track)
    suspend fun deletePlaylist(playList: Playlist)
    fun saveAlbumCover(uri: String, fileName: String): String
    suspend fun addNewPlaylist(name: String, description: String?, path: String?)
}
