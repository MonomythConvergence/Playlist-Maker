package com.example.playlistmaker.editPlaylist.domain

interface ConvertBitmapInteractor {
    suspend fun convertPathToBitmap(path: String, pathToBitmapConverterCallback: PathToBitmapConverterCallback)
}