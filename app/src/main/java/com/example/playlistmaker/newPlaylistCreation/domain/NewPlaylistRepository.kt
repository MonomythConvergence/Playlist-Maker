package com.example.playlistmaker.newPlaylistCreation.domain

interface NewPlaylistRepository {
    fun saveAlbumCover(uri: String, fileName: String): String
    suspend fun addNewPlaylist(name: String, description: String?, path: String?)
}
