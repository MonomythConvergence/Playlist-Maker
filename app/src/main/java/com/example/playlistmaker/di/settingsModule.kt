package com.example.playlistmaker.di

import com.example.playlistmaker.settings.data.ThemeSwitchRepositoryImpl
import com.example.playlistmaker.settings.domain.ThemeSwitchRepository
import com.example.playlistmaker.settings.domain.ThemeSwitchInteractor
import com.example.playlistmaker.settings.domain.ThemeSwitchInteractorImpl
import com.example.playlistmaker.settings.ui.SettingsViewModel
import com.example.playlistmaker.sharing.data.ExternalInteractionHandlerImpl
import com.example.playlistmaker.sharing.domain.ExternalInteractionHandler
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsModule = module {
    viewModelOf(::SettingsViewModel)
    singleOf(::ThemeSwitchRepositoryImpl) bind ThemeSwitchRepository::class
    singleOf(::ExternalInteractionHandlerImpl) bind ExternalInteractionHandler::class
    singleOf(::ThemeSwitchInteractorImpl) bind ThemeSwitchInteractor::class
}