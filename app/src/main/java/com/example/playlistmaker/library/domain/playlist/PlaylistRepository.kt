package com.example.playlistmaker.library.domain.playlist

import com.example.playlistmaker.library.data.dto.PlaylistDTO
import com.example.playlistmaker.search.data.datamodels.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    abstract fun getPlaylists(): Flow<List<PlaylistDTO>>
    abstract suspend fun addTrackToPlaylist(playList: PlaylistDTO, track: Track)

}
