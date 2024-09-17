package com.example.playlistmaker.newPlaylistCreation.domain

import android.net.Uri

interface NewPlaylistInteractor {
    abstract fun saveAlbumCover(bitmap: Uri, fileName: String): String
    abstract suspend fun addNewPlaylist(name: String, description: String?, path: String?)
}
