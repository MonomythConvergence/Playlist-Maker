package com.example.playlistmaker.library.data

import com.example.playlistmaker.library.data.dto.PlaylistDTO
import com.example.playlistmaker.library.domain.playlist.Playlist
import com.example.playlistmaker.library.domain.playlist.PlaylistMapper
import java.util.Date

class PlaylistMapperImpl : PlaylistMapper {

    override fun toDomainModel(dto: Any): Playlist {
        require(dto is PlaylistDTO)
        return Playlist(
            playlistId = dto.playlistId,
            playlistTitle = dto.playlistTitle,
            playlistDescriptor = dto.playlistDescriptor,
            coverImagePath = dto.coverImagePath,
            trackList = dto.trackList,
            trackCount = dto.trackCount,
            entryTime = dto.entryTime
        )
    }

    override fun toDTO(domainModel: Playlist): PlaylistDTO {
        val entryTime = if (domainModel.entryTime.toInt() ==-1)
        {Date().time}
        else {domainModel.entryTime}
        return PlaylistDTO(
            playlistId = domainModel.playlistId,
            playlistTitle = domainModel.playlistTitle,
            playlistDescriptor = domainModel.playlistDescriptor,
            coverImagePath = domainModel.coverImagePath,
            trackList = domainModel.trackList,
            trackCount = domainModel.trackCount,
            entryTime = entryTime
        )
    }
}