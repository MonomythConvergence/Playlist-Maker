package com.example.playlistmaker
import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.playlistmaker.settings.data.ThemeSwitchRepositoryImpl
import com.example.playlistmaker.settings.ui.ThemeViewModel

class App : Application() {
    companion object {
        var darkTheme: Boolean = false
        lateinit var recentTracksSharedPreferences: SharedPreferences
    }

    private lateinit var themeSharedPreferences: SharedPreferences
    private lateinit var themeViewModel: ThemeViewModel


    override fun onCreate() {
        super.onCreate()

        themeSharedPreferences = getSharedPreferences(Constants.THEME_PREF_KEY, MODE_PRIVATE)
        themeViewModel = ThemeViewModel(ThemeSwitchRepositoryImpl(this))
        darkTheme = themeSharedPreferences.getBoolean(Constants.THEME_PREF_KEY, false)
        switchTheme(darkTheme)

        recentTracksSharedPreferences =
            getSharedPreferences(Constants.RECENT_TRACKS_KEY, MODE_PRIVATE)

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

