package com.example.playlistmaker.editPlaylist.data

import android.content.Context
import android.graphics.BitmapFactory
import com.example.playlistmaker.editPlaylist.domain.ConvertBitmapRepository
import com.example.playlistmaker.editPlaylist.domain.PathToBitmapConverterCallback
import java.io.File

class ConvertBitmapRepositoryImpl(val context: Context) : ConvertBitmapRepository {
    override suspend fun convertPathToBitmap(
        path: String,
        pathToBitmapConverterCallback: PathToBitmapConverterCallback
    ) {
        val imgFile = File(path)
        if (imgFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            pathToBitmapConverterCallback.convertPathToBitmap(bitmap)
        }
    }

}