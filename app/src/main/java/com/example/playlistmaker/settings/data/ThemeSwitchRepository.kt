package com.example.playlistmaker.settings.data

interface ThemeSwitchRepository {
    fun switchTheme(switchStateOn : Boolean)
    fun checkOnState(): Boolean
}