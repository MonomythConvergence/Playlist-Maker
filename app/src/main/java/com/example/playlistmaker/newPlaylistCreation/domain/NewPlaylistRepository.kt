package com.example.playlistmaker.newPlaylistCreation.domain

import android.net.Uri

interface NewPlaylistRepository {
    abstract fun saveAlbumCover(uri: Uri, fileName: String): String
    abstract suspend fun addNewPlaylist(name: String, description: String?, path: String?)
}
