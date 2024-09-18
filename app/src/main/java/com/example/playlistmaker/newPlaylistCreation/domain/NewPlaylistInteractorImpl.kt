package com.example.playlistmaker.newPlaylistCreation.domain

class NewPlaylistInteractorImpl(val newPlaylistRepository : NewPlaylistRepository): NewPlaylistInteractor {

    override fun saveAlbumCover(bitmap: String, fileName: String): String {
        return newPlaylistRepository.saveAlbumCover(bitmap, fileName)
    }

    override suspend fun addNewPlaylist(name: String, description: String?, path: String?) {
        newPlaylistRepository.addNewPlaylist(name,description,path)
    }
}