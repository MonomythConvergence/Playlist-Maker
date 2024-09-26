package com.example.playlistmaker.library.ui.playlists

import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.ui.TrackMapper
import com.example.playlistmaker.search.ui.TrackParcelable

class PlaylistMapper(val trackMapper: TrackMapper) {

    private fun convertListToDomain(list: List<TrackParcelable>): List<Track> {

        //trackList = trackList.map { track -> TrackMapper.toParcelable(track) }, подвел
        // и не компилируется, чтобы не ждать - решение на коленке

        var newList: MutableList<Track> = mutableListOf()
        for (track in list) {
            val convertedTrack = trackMapper.toDomain(track)
            newList.add(convertedTrack)
        }

        return newList.toList()
    }

    private fun convertListToParcelable(list: List<Track>): List<TrackParcelable> {

        var newList: MutableList<TrackParcelable> = mutableListOf()
        for (track in list) {
            val convertedTrack = trackMapper.toParcelable(track)
            newList.add(convertedTrack)
        }

        return newList.toList()
    }

    fun toParcelable(playlist: Playlist): PlaylistParcelable {
        return PlaylistParcelable(
            playlistId = playlist.playlistId,
            playlistTitle = playlist.playlistTitle,
            playlistDescriptor = playlist.playlistDescriptor,
            coverImagePath = playlist.coverImagePath,
            trackIDlist = playlist.trackIDlist,
            trackList = convertListToParcelable(playlist.trackList),
            trackCount = playlist.trackCount,
            entryTime = playlist.entryTime
        )
    }

    fun toDomain(playlist: PlaylistParcelable): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            playlistTitle = playlist.playlistTitle,
            playlistDescriptor = playlist.playlistDescriptor,
            coverImagePath = playlist.coverImagePath,
            trackIDlist = playlist.trackIDlist,
            trackList = convertListToDomain(playlist.trackList),
            trackCount = playlist.trackCount,
            entryTime = playlist.entryTime
        )
    }
}

