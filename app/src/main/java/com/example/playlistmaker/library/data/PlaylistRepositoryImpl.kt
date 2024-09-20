package com.example.playlistmaker.library.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.playlistmaker.db.PlaylistDao
import com.example.playlistmaker.db.PlaylistEntityConverter
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlist.PlaylistRepository
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistEntityConverter: PlaylistEntityConverter,
    val context: Context

) : PlaylistRepository {

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getPlaylists().map { playlistEntities ->
            playlistEntities.map { playlistEntityConverter.fromEntity(it) }
        }
    }

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        if (playlist.trackList.contains(track)) {
            return
        }

        var newTrackList = playlist.trackList.toMutableList()
        newTrackList.clear()
        newTrackList.add(track)
        var newIDList = playlist.trackIDlist.toMutableList()
        newIDList.clear()
        newIDList.add(track.trackId)

        val updatedPlaylist = playlist.copy(
            trackList = newTrackList + playlist.trackList,
            trackIDlist = newIDList +playlist.trackIDlist,
            trackCount = playlist.trackCount + 1
        )
        playlistDao.updatePlaylist(playlistEntityConverter.toEntity(updatedPlaylist))
    }

    override suspend fun removeTrackFromPlaylist(playlist: Playlist, track: Track) {
        if (!playlist.trackList.contains(track)) {
            return
        }

        val updatedPlaylist = playlist.copy(
            trackList = playlist.trackList - track,
            trackIDlist = playlist.trackIDlist - track.trackId,
            trackCount = playlist.trackCount - 1
        )
        playlistDao.updatePlaylist(playlistEntityConverter.toEntity(updatedPlaylist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.updatePlaylist(playlistEntityConverter.toEntity(playlist))
    }

    override fun getPlaylistByID(playlistID: Long): Flow<Playlist?> {
        return playlistDao.getPlaylistByID(playlistID).map { playlistEntity ->
            playlistEntity?.let { playlistEntityConverter.fromEntity(it) }
        }
    }

    override suspend fun deletePlaylist(playList: Playlist) {
        playlistDao.deletePlaylist(playlistEntityConverter.toEntity(playList))
    }

    override fun saveAlbumCover(uri: String, fileName: String): String {

        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        val timestamp = dateFormat.format(Date())

        val file = File(directory, "${fileName}_${timestamp}.png")
        Log.d("MyTag","${fileName}_${timestamp}.png")//todo del

        val outputStream = FileOutputStream(file)
        uriToBitmap(Uri.parse(uri)).compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return file.absolutePath

    }

    override suspend fun addNewPlaylist(name: String, description: String?, path: String?) {
        val playlist = Playlist(
            playlistId = -1,
            playlistTitle = name,
            playlistDescriptor = description ?: "",
            coverImagePath = path ?: "",
            trackList = ArrayList(),
            trackIDlist = emptyList(),
            trackCount = 0,
            entryTime = -1

        )
        playlistDao.addPlaylist(playlistEntityConverter.toEntity(playlist))
    }

    private fun uriToBitmap(uri: Uri): Bitmap {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }


}


