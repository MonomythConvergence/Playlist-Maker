package com.example.playlistmaker.library.domain.playlist

import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(val playlistRepository: PlaylistRepository) : PlaylistInteractor {
    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playList: Playlist, track: Track) {
        playlistRepository.addTrackToPlaylist(playList,track)
    }

    override fun getPlaylistByID(playlistID: Long): Flow<Playlist?> {
        return playlistRepository.getPlaylistByID(playlistID)
    }
}