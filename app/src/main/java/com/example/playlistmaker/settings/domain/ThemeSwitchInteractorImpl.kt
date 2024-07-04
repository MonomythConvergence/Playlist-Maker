package com.example.playlistmaker.settings.domain

import com.example.playlistmaker.settings.data.ThemeSwitchRepository

class ThemeSwitchInteractorImpl(private val themeSwitchRepository: ThemeSwitchRepository) : ThemeSwitchInteractor{
    override fun switchTheme(switchStateOn: Boolean) {
        themeSwitchRepository.switchTheme(switchStateOn)
    }

    override fun checkOnState(): Boolean{
        return themeSwitchRepository.checkOnState()
    }
}