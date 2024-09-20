package com.example.playlistmaker.di

import com.example.playlistmaker.editPlaylist.data.ConvertBitmapRepositoryImpl
import com.example.playlistmaker.editPlaylist.domain.ConvertBitmapInteractor
import com.example.playlistmaker.editPlaylist.domain.ConvertBitmapInteractorImpl
import com.example.playlistmaker.editPlaylist.domain.ConvertBitmapRepository
import com.example.playlistmaker.editPlaylist.ui.EditPlaylistViewModel
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.player.ui.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val editPlaylistModule = module {
    single<ConvertBitmapRepository> { ConvertBitmapRepositoryImpl(get()) }
    single<ConvertBitmapInteractor> { ConvertBitmapInteractorImpl(get()) }

    viewModel<EditPlaylistViewModel> { EditPlaylistViewModel(get(),get(),get())}
}