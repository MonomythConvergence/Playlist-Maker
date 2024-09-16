package com.example.playlistmaker.library.data

import com.example.playlistmaker.db.PlaylistDBEntityConverter
import com.example.playlistmaker.db.PlaylistDao
import com.example.playlistmaker.db.PlaylistEntity
import com.example.playlistmaker.library.data.dto.PlaylistDTO
import com.example.playlistmaker.library.domain.playlist.PlaylistRepository
import com.example.playlistmaker.search.data.datamodels.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    val playlistDBEntityConverter: PlaylistDBEntityConverter,
    val playlistDao: PlaylistDao
) : PlaylistRepository {

    override fun getPlaylists(): Flow<List<PlaylistDTO>> {
        return playlistDao.getPlaylists().map { playlistEntities: List<PlaylistEntity> ->
            playlistEntities.map { playlistDBEntityConverter.map(it) }
        }
    }
    override suspend fun addTrackToPlaylist(playList: PlaylistDTO, track: Track) {
        if (!playList.trackList.contains(track.trackId)){
            val mutableTrackList = playList.trackList.toMutableList()
            mutableTrackList.add(track.trackId)
            val updatedPlayList = playList.copy(trackList = mutableTrackList, trackCount = playList.trackCount + 1)
            playlistDao.updatePlaylist(playlistDBEntityConverter.map(updatedPlayList))
    }}
}
