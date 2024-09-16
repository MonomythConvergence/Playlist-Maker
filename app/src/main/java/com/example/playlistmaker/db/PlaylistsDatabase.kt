package com.example.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(version = 1, entities = [PlaylistEntity::class])
@TypeConverters(TrackListGsonConverter::class)
abstract class PlaylistsDatabase : RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao

}
