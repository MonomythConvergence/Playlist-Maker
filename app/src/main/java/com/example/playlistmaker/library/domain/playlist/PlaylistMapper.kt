package com.example.playlistmaker.library.domain.playlist

interface PlaylistMapper {
    fun toDomainModel(dto: Any): Playlist
    fun toDTO(domainModel: Playlist): Any
}