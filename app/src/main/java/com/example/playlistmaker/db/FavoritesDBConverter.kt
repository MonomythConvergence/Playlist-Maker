package com.example.playlistmaker.db

import com.example.playlistmaker.search.data.datamodels.Track
import java.util.Date

class FavoritesDBConverter {
    fun map(track: Track): SongEntity {
        return SongEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            Date().time
        )
    }

    fun map(song: SongEntity): Track {
        return Track(
            song.trackName,
            song.artistName,
            song.trackTime,
            song.artworkUrl100,
            song.trackId,
            song.collectionName,
            song.releaseDate,
            song.primaryGenreName,
            song.country,
            song.previewUrl
        )
    }
}