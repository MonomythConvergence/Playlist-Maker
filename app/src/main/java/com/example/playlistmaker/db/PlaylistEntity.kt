package com.example.playlistmaker.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

@Entity(tableName = "playlists_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = -1,
    val playlistTitle: String,
    val playlistDescriptor: String,
    val coverImagePath: String,
    var trackIDlist : List<Long>,
    var trackList: List<Track>,
    var trackCount: Int,
    val entryTime: Long = Date().time
)

class TrackListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromTrackList(trackList: List<Track>): String {
        return gson.toJson(trackList)
    }

    @TypeConverter
    fun toTrackList(trackListJson: String): List<Track> {
        val type = object : TypeToken<List<Track>>() {}.type
        return gson.fromJson(trackListJson, type)
    }
}

class ListLongConverter {
    private val gson = Gson()
    @TypeConverter
    fun fromList(list: List<Long>): String {
        return gson.toJson(list)
    }
    @TypeConverter
    fun toList(json: String): List<Long> {
        val type = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(json, type)
    }}

class PlaylistEntityConverter {
    @TypeConverter
    fun fromEntity(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlistEntity.playlistId,
            playlistTitle = playlistEntity.playlistTitle,
            playlistDescriptor = playlistEntity.playlistDescriptor,
            coverImagePath = playlistEntity.coverImagePath,
            trackIDlist = playlistEntity.trackIDlist,
            trackList = playlistEntity.trackList,
            trackCount = playlistEntity.trackCount,
            entryTime = playlistEntity.entryTime
        )
    }
    @TypeConverter
    fun toEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = if (playlist.playlistId == -1L) 0 else playlist.playlistId,
            playlistTitle = playlist.playlistTitle,
            playlistDescriptor = playlist.playlistDescriptor,
            coverImagePath = playlist.coverImagePath,
            trackIDlist = playlist.trackIDlist,
            trackList = playlist.trackList,
            trackCount = playlist.trackCount,
            entryTime = if (playlist.entryTime == -1L) Date().time else playlist.entryTime
        )
    }
}
