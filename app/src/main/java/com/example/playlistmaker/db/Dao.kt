package com.example.playlistmaker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Insert(entity = SongEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongIntoFavorites(song: SongEntity)

    @Query("SELECT * FROM favorites_table ORDER BY entryTime DESC")
    fun getFavorites(): Flow<List<SongEntity>>

    @Delete
    suspend fun deleteSongFromFavorites(song: SongEntity)

    @Query("SELECT trackId FROM favorites_table")
    fun getFavoritesIDList(): Flow<List<Long>>
}