package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.db.TrackDBEntityConverter
import com.example.playlistmaker.db.FavoritesDatabase
import com.example.playlistmaker.db.PlaylistEntityConverter
import com.example.playlistmaker.library.domain.favorites.FavoritesRepository
import com.example.playlistmaker.library.data.FavoritesRepositoryImpl
import com.example.playlistmaker.library.data.PlaylistRepositoryImpl
import com.example.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.library.domain.playlist.PlaylistInteractorImpl
import com.example.playlistmaker.library.domain.playlist.PlaylistRepository
import com.example.playlistmaker.library.domain.favorites.FavoritesInteractor
import com.example.playlistmaker.library.domain.favorites.FavoritesInteractorImpl
import com.example.playlistmaker.library.ui.favorites.FavoritesFragmentViewModel
import com.example.playlistmaker.library.ui.playlists.PlaylistMapper
import com.example.playlistmaker.library.ui.playlists.PlaylistsFragmentViewModel
import com.example.playlistmaker.search.ui.TrackMapper
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val libraryModule = module {
        viewModelOf(::FavoritesFragmentViewModel)
        viewModelOf(::PlaylistsFragmentViewModel)

        singleOf(::FavoritesInteractorImpl) bind FavoritesInteractor::class

        singleOf(::FavoritesRepositoryImpl) {
            bind<FavoritesRepository>()
        }

        singleOf(::TrackDBEntityConverter)
        singleOf(::PlaylistEntityConverter)

        singleOf(::PlaylistInteractorImpl) {
            bind<PlaylistInteractor>()
        }

        singleOf(::PlaylistRepositoryImpl) {
            bind<PlaylistRepository>()
        }

    single {
        Room.databaseBuilder(androidContext(), FavoritesDatabase::class.java, "favorites.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<FavoritesDatabase>().favoriteDao() }

    singleOf(::TrackMapper)

    singleOf(::PlaylistMapper)
}