package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.db.TrackDBEntityConverter
import com.example.playlistmaker.db.FavoritesDatabase
import com.example.playlistmaker.db.PlaylistDBEntityConverter
import com.example.playlistmaker.library.domain.favorites.FavoritesRepository
import com.example.playlistmaker.library.data.FavoritesRepositoryImpl
import com.example.playlistmaker.library.data.PlaylistRepositoryImpl
import com.example.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.library.domain.playlist.PlaylistInteractorImpl
import com.example.playlistmaker.library.domain.playlist.PlaylistRepository
import com.example.playlistmaker.library.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.library.domain.favorites.FavoritesInteractorImpl
import com.example.playlistmaker.library.ui.favorites.FavoritesFragmentViewModel
import com.example.playlistmaker.library.ui.playlists.PlaylistsFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryModule = module {
    viewModel { FavoritesFragmentViewModel(get()) }
    viewModel { PlaylistsFragmentViewModel(get()) }
    single<FavoritesInteractor> { FavoritesInteractorImpl(get()) }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get(), get()) }
    single<TrackDBEntityConverter> { TrackDBEntityConverter() }
    single<PlaylistInteractor> { PlaylistInteractorImpl(get()) }
    single<PlaylistRepository> { PlaylistRepositoryImpl(get(), get()) }
    single<PlaylistDBEntityConverter> { PlaylistDBEntityConverter() }

    single {
        Room.databaseBuilder(androidContext(), FavoritesDatabase::class.java, "favorites.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<FavoritesDatabase>().favoriteDao() }
}