package com.example.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.example.playlistmaker.db.FavoritesDatabase
import com.example.playlistmaker.db.PlaylistDao
import com.example.playlistmaker.db.PlaylistsDatabase
import com.example.playlistmaker.newPlaylistCreation.data.NewPlaylistRepositoryImpl
import com.example.playlistmaker.newPlaylistCreation.domain.NewPlaylistInteractor
import com.example.playlistmaker.newPlaylistCreation.domain.NewPlaylistInteractorImpl
import com.example.playlistmaker.newPlaylistCreation.domain.NewPlaylistRepository
import com.example.playlistmaker.newPlaylistCreation.ui.NewPlaylistViewModel
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val newPlaylistModule = module {
    single<NewPlaylistRepository> { NewPlaylistRepositoryImpl(get<PlaylistDao>()) }
    single<NewPlaylistInteractor> { NewPlaylistInteractorImpl(get<NewPlaylistRepository>()) }
    viewModel<NewPlaylistViewModel> { NewPlaylistViewModel(get<NewPlaylistInteractor>()) }

    single {
        Room.databaseBuilder(androidContext(), PlaylistsDatabase::class.java, "playlists.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<PlaylistsDatabase>().playlistDao() }
}
