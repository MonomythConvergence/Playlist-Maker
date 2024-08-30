package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.db.FavoritesDBConverter
import com.example.playlistmaker.db.FavoritesDatabase
import com.example.playlistmaker.library.domain.FavoritesRepository
import com.example.playlistmaker.library.data.FavoritesRepositoryImpl
import com.example.playlistmaker.library.ui.FavoritesFragmentViewModel
import com.example.playlistmaker.library.ui.PlaylistsFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {
    viewModel { FavoritesFragmentViewModel(get()) }
    viewModel { PlaylistsFragmentViewModel() }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get()) }
    single<FavoritesDBConverter> { FavoritesDBConverter() }

    single {
        Room.databaseBuilder(androidContext(), FavoritesDatabase::class.java, "favorites.db")
            .build()
    }
    single { get<FavoritesDatabase>().songDao() }
}