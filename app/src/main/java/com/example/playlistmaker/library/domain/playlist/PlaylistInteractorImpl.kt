package com.example.playlistmaker.library.domain.playlist

import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) : PlaylistInteractor {

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlistRepository.addTrackToPlaylist(playlist, track)
    }

    override fun getPlaylistByID(playlistID: Long): Flow<Playlist?> {
        return playlistRepository.getPlaylistByID(playlistID)
    }

    override suspend fun removeTrackFromPlaylist(playlist: Playlist, trackToDelete: Track) {
        playlistRepository.removeTrackFromPlaylist(playlist, trackToDelete)
    }

    override suspend fun deletePlaylist(playlistToDelete: Playlist) {
        playlistRepository.deletePlaylist(playlistToDelete)
    }

    override fun saveAlbumCover(bitmap: String, fileName: String): String {
        return playlistRepository.saveAlbumCover(bitmap, fileName)
    }

    override suspend fun addNewPlaylist(name: String, description: String?, path: String?) {
        playlistRepository.addNewPlaylist(name,description,path)
    }


}
