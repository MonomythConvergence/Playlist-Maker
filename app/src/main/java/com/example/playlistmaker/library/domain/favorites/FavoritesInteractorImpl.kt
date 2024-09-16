package com.example.playlistmaker.library.domain.favorites

import com.example.playlistmaker.search.data.datamodels.Track
import kotlinx.coroutines.flow.Flow

class FavoritesInteractorImpl(private val repository: FavoritesRepository) : FavoritesInteractor {
    override suspend fun addTrackToFavorites(track: Track) {
        repository.addTrackToFavorites(track)
    }

    override fun getFavorites(): Flow<List<Track>> {
        return repository.getFavorites()
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        repository.deleteTrackFromFavorites(track)
    }

    override fun getFavoritesIDList(): Flow<List<Long>> {
        return repository.getFavoritesIDList()
    }
}