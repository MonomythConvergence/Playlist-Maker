package com.example.playlistmaker.settings.domain

class ThemeSwitchInteractorImpl(private val themeSwitchRepository: ThemeSwitchRepository) : ThemeSwitchInteractor{
    override fun switchTheme(switchStateOn: Boolean) {
        themeSwitchRepository.switchTheme(switchStateOn)
    }

    override fun checkOnState(): Boolean{
        return themeSwitchRepository.checkOnState()
    }
}