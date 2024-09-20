package com.example.playlistmaker.di

import androidx.room.Room
import com.example.playlistmaker.db.PlaylistsDatabase
import com.example.playlistmaker.library.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.newPlaylistCreation.ui.NewPlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val newPlaylistModule = module {

    viewModel<NewPlaylistViewModel> { NewPlaylistViewModel(get<PlaylistInteractor>(),get()) }

    single {
        Room.databaseBuilder(androidContext(), PlaylistsDatabase::class.java, "playlists.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<PlaylistsDatabase>().playlistDao() }
}
