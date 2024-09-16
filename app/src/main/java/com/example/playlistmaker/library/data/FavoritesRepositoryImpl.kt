package com.example.playlistmaker.library.data

import com.example.playlistmaker.db.TrackDBEntityConverter
import com.example.playlistmaker.db.FavoritesDao
import com.example.playlistmaker.db.FavoriteEntity
import com.example.playlistmaker.library.domain.favorites.FavoritesRepository
import com.example.playlistmaker.search.data.datamodels.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val favoritesDao: FavoritesDao,
    private val trackDBEntityConverter: TrackDBEntityConverter
) : FavoritesRepository {

    override suspend fun addTrackToFavorites(track: Track) {
        val song = trackDBEntityConverter.map(track)
        favoritesDao.insertSongIntoFavorites(song)
    }


    override fun getFavorites(): Flow<List<Track>> {
        return favoritesDao.getFavorites().map { songList: List<FavoriteEntity> ->
            songList.map { trackDBEntityConverter.map(it) }
        }
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        val song = trackDBEntityConverter.map(track)
        favoritesDao.deleteSongFromFavorites(song)
    }

    override fun getFavoritesIDList(): Flow<List<Long>> {
        return favoritesDao.getFavoritesIDList()
    }

}