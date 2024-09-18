package com.example.playlistmaker.db

import com.example.playlistmaker.library.data.dto.PlaylistDTO

class PlaylistDBEntityConverter {
    fun map(playlist: PlaylistDTO): PlaylistEntity {
        return PlaylistEntity(
            playlist.playlistId,
            playlist.playlistTitle,
            playlist.playlistDescriptor,
            playlist.coverImagePath,
            playlist.trackList,
            playlist.trackCount,
            playlist.entryTime,
        )
    }

    fun map(entity: PlaylistEntity): PlaylistDTO {
        return PlaylistDTO(
            entity.playlistId,
            entity.playlistTitle,
            entity.playlistDescriptor,
            entity.coverImagePath,
            entity.trackList,
            entity.trackCount,
            entity.entryTime,
        )
    }
}
