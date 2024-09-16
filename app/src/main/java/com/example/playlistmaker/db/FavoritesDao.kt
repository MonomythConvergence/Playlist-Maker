package com.example.playlistmaker.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Insert(entity = FavoriteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongIntoFavorites(song: FavoriteEntity)

    @Query("SELECT * FROM favorites_table ORDER BY entryTime DESC")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteSongFromFavorites(song: FavoriteEntity)

    @Query("SELECT trackId FROM favorites_table")
    fun getFavoritesIDList(): Flow<List<Long>>
}