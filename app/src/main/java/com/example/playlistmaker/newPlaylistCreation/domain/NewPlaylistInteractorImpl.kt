package com.example.playlistmaker.newPlaylistCreation.domain

import android.net.Uri

class NewPlaylistInteractorImpl(val newPlaylistRepository : NewPlaylistRepository): NewPlaylistInteractor {

    override fun saveAlbumCover(bitmap: Uri, fileName: String): String {
        return newPlaylistRepository.saveAlbumCover(bitmap, fileName)
    }

    override suspend fun addNewPlaylist(name: String, description: String?, path: String?) {
        newPlaylistRepository.addNewPlaylist(name,description,path)
    }
}