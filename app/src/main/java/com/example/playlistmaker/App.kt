package com.example.playlistmaker
import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.playlistmaker.di.playerModule
import com.example.playlistmaker.di.searchModule
import com.example.playlistmaker.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    companion object {
        var darkTheme: Boolean = false
    }

    private lateinit var themeSharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(settingsModule,searchModule, playerModule)
        }

        themeSharedPreferences = getSharedPreferences(Constants.THEME_PREF_KEY, MODE_PRIVATE)

        darkTheme = themeSharedPreferences.getBoolean(Constants.THEME_PREF_KEY, false)
        switchTheme(darkTheme)

    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
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

