package com.example.playlistmaker.settings.domain

interface ThemeSwitchRepository {
    fun switchTheme(switchStateOn : Boolean)
    fun checkOnState(): Boolean
}