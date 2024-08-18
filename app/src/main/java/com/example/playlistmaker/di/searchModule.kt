package com.example.playlistmaker.di

import com.example.playlistmaker.search.data.APICLientProvider
import com.example.playlistmaker.search.data.PreferencesManager
import com.example.playlistmaker.search.data.PreferencesManagerImpl
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitApiClient
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractorImpl
import com.example.playlistmaker.search.ui.SearchViewModel
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {
    viewModel<SearchViewModel> { SearchViewModel(get()) }
    single<SearchInteractor> { SearchInteractorImpl(get()) }
    single<SearchRepository> { SearchRepositoryImpl(get(), get(),get()) }
    single<PreferencesManager> { PreferencesManagerImpl(androidContext()) }
    single<RetrofitApiClient> { APICLientProvider.provideApiClient() }
    single<Gson> { Gson() }
}