package com.example.playlistmaker.library.data

import com.example.playlistmaker.db.FavoritesDBConverter
import com.example.playlistmaker.db.SongDao
import com.example.playlistmaker.db.SongEntity
import com.example.playlistmaker.library.domain.FavoritesRepository
import com.example.playlistmaker.search.data.datamodels.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val songDao: SongDao,
    private val favoritesDBConverter: FavoritesDBConverter
) : FavoritesRepository {

    override suspend fun addTrackToFavorites(track: Track) {
        val song = favoritesDBConverter.map(track)
        songDao.insertSongIntoFavorites(song)
    }


    override fun getFavorites(): Flow<List<Track>> {
        return songDao.getFavorites().map { songList: List<SongEntity> ->
            songList.map { favoritesDBConverter.map(it) }
        }
    }

    override suspend fun deleteTrackFromFavorites(track: Track) {
        val song = favoritesDBConverter.map(track)
        songDao.deleteSongFromFavorites(song)
    }

    override fun getFavoritesIDList(): Flow<List<Long>> {
        return songDao.getFavoritesIDList()
    }

}