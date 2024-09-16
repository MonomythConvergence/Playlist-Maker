package com.example.playlistmaker.library.domain.playlist

import com.example.playlistmaker.library.data.dto.PlaylistDTO
import com.example.playlistmaker.search.data.datamodels.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(val playlistRepository: PlaylistRepository) : PlaylistInteractor {
    override fun getPlaylists(): Flow<List<PlaylistDTO>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun addTrackToPlaylist(playList: PlaylistDTO, track: Track) {
        playlistRepository.addTrackToPlaylist(playList,track)
    }
}