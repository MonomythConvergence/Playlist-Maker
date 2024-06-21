package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.settings.domain.ThemeSwitchRepository

class ThemeViewModelFactory(private val themeSwitchRepository: ThemeSwitchRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
            return ThemeViewModel(themeSwitchRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}