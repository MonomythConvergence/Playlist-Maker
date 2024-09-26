package com.example.playlistmaker.editPlaylist.domain

interface PathToBitmapConverterCallback{
    suspend fun convertPathToBitmap(bitmap : Any)
}