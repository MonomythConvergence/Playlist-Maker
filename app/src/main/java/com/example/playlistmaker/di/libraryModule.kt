package com.example.playlistmaker.di

import com.example.playlistmaker.library.ui.FavoritesFragmentViewModel
import com.example.playlistmaker.library.ui.PlaylistsFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {
    viewModel { FavoritesFragmentViewModel()}
    viewModel { PlaylistsFragmentViewModel() }
}