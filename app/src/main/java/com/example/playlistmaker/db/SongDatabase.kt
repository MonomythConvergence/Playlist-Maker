package com.example.playlistmaker.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [SongEntity::class])
abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun songDao(): SongDao

}
