package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.ui.SearchHistory
import com.example.playlistmaker.settings.data.ThemeSwitchRepositoryImpl
import com.example.playlistmaker.settings.ui.ThemeViewModel

class App : Application() {
    companion object {
        var darkTheme: Boolean = false
        lateinit var recentTracksSharedPreferences: SharedPreferences
    }

    private lateinit var themeSharedPreferences: SharedPreferences
    private lateinit var themeViewModel: ThemeViewModel
    private lateinit var mediaPlayerRepository: MediaPlayerRepository
    private lateinit var mediaPlayerInteractor: MediaPlayerInteractor

    fun initializeMediaPlayerinstances(url: String) {
        mediaPlayerRepository = MediaPlayerRepositoryImpl(url)
       mediaPlayerInteractor = MediaPlayerInteractorImpl(mediaPlayerRepository)
    }

    fun giveMediaPlayerInteractor(): MediaPlayerInteractor {
        return mediaPlayerInteractor
    }

    override fun onCreate() {
        super.onCreate()

        themeSharedPreferences = getSharedPreferences(Constants.THEME_PREF_KEY, MODE_PRIVATE)
        themeViewModel = ThemeViewModel(ThemeSwitchRepositoryImpl(this))
        darkTheme = themeSharedPreferences.getBoolean(Constants.THEME_PREF_KEY, false)
        switchTheme(darkTheme)

        recentTracksSharedPreferences =
            getSharedPreferences(Constants.RECENT_TRACKS_KEY, MODE_PRIVATE)
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
            }
        )

    }
}

