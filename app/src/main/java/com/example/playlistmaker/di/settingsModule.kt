package com.example.playlistmaker.di

import com.example.playlistmaker.settings.data.ThemeSwitchRepositoryImpl
import com.example.playlistmaker.settings.domain.ThemeSwitchRepository
import com.example.playlistmaker.settings.ui.SettingsViewModel
import com.example.playlistmaker.sharing.data.ExternalInteractionHandlerImpl
import com.example.playlistmaker.sharing.domain.ExternalInteractionHandler
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<ThemeSwitchRepository> { ThemeSwitchRepositoryImpl(get()) }
    viewModel { SettingsViewModel(get(),get()) }
    single<ExternalInteractionHandler> { ExternalInteractionHandlerImpl(get()) }
}