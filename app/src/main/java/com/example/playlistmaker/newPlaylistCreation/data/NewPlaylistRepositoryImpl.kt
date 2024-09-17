package com.example.playlistmaker.newPlaylistCreation.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.db.PlaylistDao
import com.example.playlistmaker.db.PlaylistEntity
import com.example.playlistmaker.newPlaylistCreation.domain.NewPlaylistRepository
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class NewPlaylistRepositoryImpl(val context: Context, private val playlistDao: PlaylistDao) :
    NewPlaylistRepository {

    override fun saveAlbumCover(uri: Uri, fileName: String): String {

        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (!directory.exists()) {
            directory.mkdirs()
        }


        val file = File(directory, "${fileName}.png")

        val outputStream = FileOutputStream(file)
        uriToBitmap(uri).compress(Bitmap.CompressFormat.PNG, 100, outputStream)
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

    private fun uriToBitmap(uri: Uri): Bitmap {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }
}


