package com.example.playlistmaker.library.domain

import com.example.playlistmaker.search.data.datamodels.Track
import kotlinx.coroutines.flow.Flow

interface FavoritesInteractor {
    suspend fun addTrackToFavorites(track: Track)

    fun getFavorites(): Flow<List<Track>>

    suspend fun deleteTrackFromFavorites(track: Track)

    fun getFavoritesIDList(): Flow<List<Long>>
}