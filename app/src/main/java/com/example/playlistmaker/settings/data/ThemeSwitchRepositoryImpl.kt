package com.example.playlistmaker.settings.data

import android.content.SharedPreferences
import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.example.playlistmaker.Constants
import com.example.playlistmaker.settings.domain.ThemeSwitchRepository


class ThemeSwitchRepositoryImpl(context: Context) :
    ThemeSwitchRepository {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constants.THEME_PREF_KEY, MODE_PRIVATE)


    override fun switchTheme(switchStateOn: Boolean) {
        sharedPreferences.edit {
            putBoolean(Constants.THEME_PREF_KEY, switchStateOn)
        }
        AppCompatDelegate.setDefaultNightMode(
            if (switchStateOn) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    override fun checkOnState(): Boolean {
        return sharedPreferences.getBoolean(Constants.THEME_PREF_KEY, false)
    }

}