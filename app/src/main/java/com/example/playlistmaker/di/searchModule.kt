package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.APICLientProvider
import com.example.playlistmaker.search.data.PreferencesManager
import com.example.playlistmaker.search.data.PreferencesManagerImpl
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitApiClient
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.ui.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get(), get()) }
    viewModel<SearchViewModel> { SearchViewModel(get()) }
    single<PreferencesManager> { PreferencesManagerImpl(androidContext()) }
    single<RetrofitApiClient> { APICLientProvider.provideApiClient() }
}