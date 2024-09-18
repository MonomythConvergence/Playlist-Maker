package com.example.playlistmaker.library.domain.playlist


import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    abstract fun getPlaylists(): Flow<List<Playlist>>
    abstract suspend fun addTrackToPlaylist(playList: Playlist, track: Track)
    abstract suspend fun updatePlaylist(playlist: Playlist)
    abstract fun getPlaylistByID(playlistID: Long) : Flow<Playlist?>


}
