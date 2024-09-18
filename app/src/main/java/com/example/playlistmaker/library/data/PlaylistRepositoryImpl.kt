package com.example.playlistmaker.library.data

import com.example.playlistmaker.db.PlaylistDBEntityConverter
import com.example.playlistmaker.db.PlaylistDao
import com.example.playlistmaker.db.PlaylistEntity
import com.example.playlistmaker.library.data.dto.PlaylistDTO
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlist.PlaylistMapper
import com.example.playlistmaker.library.domain.playlist.PlaylistRepository
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    val playlistDBEntityConverter: PlaylistDBEntityConverter,
    val playlistDao: PlaylistDao,
    val playlistMapper: PlaylistMapper
) : PlaylistRepository {

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getPlaylists().map { playlistEntities: List<PlaylistEntity> ->
            playlistEntities.map { playlistMapper.toDomainModel(playlistDBEntityConverter.map(it)) }
        }
    }

    override suspend fun addTrackToPlaylist(playList: Playlist, track: Track) {
        if (!playList.trackList.contains(track.trackId)){
            val mutableTrackList = playList.trackList.toMutableList()
            mutableTrackList.add(track.trackId)
            val updatedPlayList = playList.copy(trackList = mutableTrackList, trackCount = playList.trackCount + 1)
            playlistDao.updatePlaylist(playlistDBEntityConverter.map(playlistMapper.toDTO(updatedPlayList) as PlaylistDTO))
    }
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.updatePlaylist(playlistDBEntityConverter.map(playlistMapper.toDTO(playlist) as PlaylistDTO))
    }

    override fun getPlaylistByID(playlistID: Long): Flow<Playlist> {
        return playlistDao.getPlaylists().map { playlistEntities ->
            playlistEntities.find { it.playlistId == playlistID }?.let {
                playlistMapper.toDomainModel(playlistDBEntityConverter.map(it))
            }!!
        }}

}
