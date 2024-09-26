package com.example.playlistmaker.editPlaylist.domain

interface ConvertBitmapRepository {
    suspend fun convertPathToBitmap(path : String, pathToBitmapConverterCallback : PathToBitmapConverterCallback)
}
