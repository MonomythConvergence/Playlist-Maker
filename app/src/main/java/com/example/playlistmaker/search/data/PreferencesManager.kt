package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.Track

interface PreferencesManager {
    fun provideSharedPreferences(): SharedPreferences

    fun encodeRecentTrackList(newList : ArrayList<Track>)

    fun decodeRecentTrackList(): ArrayList<Track>
}
