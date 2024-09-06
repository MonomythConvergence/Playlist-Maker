package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.player.ui.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val playerModule = module {
    single<MediaPlayerRepository> { MediaPlayerRepositoryImpl() }
    single<MediaPlayerInteractor> { MediaPlayerInteractorImpl(get()) }
    viewModel<PlayerViewModel> { PlayerViewModel(get(), get())}
}