package com.example.playlistmaker.newPlaylistCreation.data

import android.graphics.Bitmap
import android.os.Environment
import com.example.playlistmaker.db.PlaylistDao
import com.example.playlistmaker.db.PlaylistEntity
import com.example.playlistmaker.newPlaylistCreation.domain.NewPlaylistRepository
import java.io.File
import java.io.FileOutputStream

class NewPlaylistRepositoryImpl(private val playlistDao: PlaylistDao) :
    NewPlaylistRepository {
    override fun saveAlbumCover(bitmap: Bitmap, fileName: String): String {
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (!directory.exists()) {
            directory.mkdirs()
        }


        val file = File(directory, "${fileName}.png")

        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        return file.absolutePath
    }

    override suspend fun addNewPlaylist(name: String, description: String?, path: String?) {
        val playlist = PlaylistEntity(
            playlistTitle = name,
            playlistDescriptor = description ?: "",
            coverImagePath = path ?: "",
            trackList = ArrayList(),
            trackCount = 0
        )
        playlistDao.addPlaylist(playlist)
    }
}


