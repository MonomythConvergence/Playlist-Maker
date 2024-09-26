package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.player.ui.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val playerModule = module {
    singleOf(::MediaPlayerRepositoryImpl) bind MediaPlayerRepository::class
    singleOf(::MediaPlayerInteractorImpl) bind MediaPlayerInteractor::class
    viewModelOf(::PlayerViewModel)
}