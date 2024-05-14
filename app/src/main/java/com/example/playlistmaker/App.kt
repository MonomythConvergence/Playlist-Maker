package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.playlistmaker.audioplayer.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.audioplayer.domain.MediaPlayerInteractor
import com.example.playlistmaker.audioplayer.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.audioplayer.domain.MediaPlayerRepository


class App : Application() {
    companion object {
        var darkTheme: Boolean = false
        lateinit var recentTracksSharedPreferences: SharedPreferences
    }
    private lateinit var themeSharedPreferences: SharedPreferences

    private lateinit var mediaPlayerRepository: MediaPlayerRepository
    lateinit var mediaPlayerInteractor: MediaPlayerInteractor

    fun initializeMediaPlayerinstances(url : String) {
        mediaPlayerRepository = MediaPlayerRepositoryImpl(url)
        mediaPlayerInteractor = MediaPlayerInteractorImpl(mediaPlayerRepository)
    }

    override fun onCreate() {
        super.onCreate()

        themeSharedPreferences = getSharedPreferences(Constants.THEME_PREF_KEY, MODE_PRIVATE)
        darkTheme =themeSharedPreferences.getBoolean(Constants.THEME_PREF_KEY, false)
        switchTheme(darkTheme)

        recentTracksSharedPreferences = getSharedPreferences(Constants.RECENT_TRACKS_KEY, MODE_PRIVATE)
        SearchHistory(recentTracksSharedPreferences).decodeAndLoad()

    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        themeSharedPreferences.edit {
            putBoolean(Constants.THEME_PREF_KEY, darkTheme)
        }
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            })

    }
}

