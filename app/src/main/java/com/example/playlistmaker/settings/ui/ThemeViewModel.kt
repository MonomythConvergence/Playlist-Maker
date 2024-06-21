package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.ThemeSwitchRepository

class ThemeViewModel (private val themeSwitchRepository: ThemeSwitchRepository) : ViewModel() {

    fun switchTheme(switchStateOn: Boolean) {
        themeSwitchRepository.switchTheme(switchStateOn)
    }

    fun checkOnState(): Boolean{

        return themeSwitchRepository.checkOnState()
    }
}