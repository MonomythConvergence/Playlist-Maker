package com.example.playlistmaker.library.domain.playlist


import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun getPlaylists(): Flow<List<Playlist>>
    suspend fun addTrackToPlaylist(playList: Playlist, track: Track)

}
