package com.example.playlistmaker.newPlaylistCreation.domain

interface NewPlaylistInteractor {
    fun saveAlbumCover(bitmap: String, fileName: String): String
    suspend fun addNewPlaylist(name: String, description: String?, path: String?)
}
