package com.example.playlistmaker.settings.domain

interface ThemeSwitchInteractor{
    fun switchTheme(switchStateOn : Boolean)
    fun checkOnState(): Boolean
}