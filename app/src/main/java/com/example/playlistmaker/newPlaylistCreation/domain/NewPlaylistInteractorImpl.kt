package com.example.playlistmaker.newPlaylistCreation.domain

import android.graphics.Bitmap

class NewPlaylistInteractorImpl(val newPlaylistRepository : NewPlaylistRepository): NewPlaylistInteractor {

    override fun saveAlbumCover(bitmap: Bitmap, fileName: String): String {
        return newPlaylistRepository.saveAlbumCover(bitmap, fileName)
    }

    override suspend fun addNewPlaylist(name: String, description: String?, path: String?) {
        newPlaylistRepository.addNewPlaylist(name,description,path)
    }
}