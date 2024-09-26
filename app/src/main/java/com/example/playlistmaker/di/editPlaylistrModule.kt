package com.example.playlistmaker.di

import com.example.playlistmaker.editPlaylist.data.ConvertBitmapRepositoryImpl
import com.example.playlistmaker.editPlaylist.domain.ConvertBitmapInteractor
import com.example.playlistmaker.editPlaylist.domain.ConvertBitmapInteractorImpl
import com.example.playlistmaker.editPlaylist.domain.ConvertBitmapRepository
import com.example.playlistmaker.editPlaylist.ui.EditPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


val editPlaylistModule = module {
    viewModelOf(::EditPlaylistViewModel)

    singleOf(::ConvertBitmapRepositoryImpl) bind ConvertBitmapRepository::class
    singleOf(::ConvertBitmapInteractorImpl) bind ConvertBitmapInteractor::class
}