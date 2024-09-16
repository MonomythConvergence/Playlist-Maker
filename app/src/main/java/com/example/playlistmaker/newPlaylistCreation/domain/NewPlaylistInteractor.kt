package com.example.playlistmaker.newPlaylistCreation.domain

import android.graphics.Bitmap

interface NewPlaylistInteractor {
    abstract fun saveAlbumCover(bitmap: Bitmap, fileName: String): String
    abstract suspend fun addNewPlaylist(name: String, description: String?, path: String?)
}
