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
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val searchModule = module {
    viewModelOf(::SearchViewModel)
    singleOf(::SearchInteractorImpl) bind SearchInteractor::class
    singleOf(::SearchRepositoryImpl) bind SearchRepository::class
    singleOf(::PreferencesManagerImpl) bind PreferencesManager::class
    single<RetrofitApiClient> { APICLientProvider.provideApiClient() } //тут наверное тоже как-то можно, но времени не особо было
    singleOf(::Gson)

}
