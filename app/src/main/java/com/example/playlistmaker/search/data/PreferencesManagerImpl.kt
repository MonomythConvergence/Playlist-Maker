package com.example.playlistmaker.search.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playlistmaker.Constants
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesManagerImpl(context: Context) : PreferencesManager {

    private val preferences = context.getSharedPreferences(Constants.RECENT_TRACKS_KEY, MODE_PRIVATE)
    override fun provideSharedPreferences(): SharedPreferences {
        return preferences
    }

    override fun encodeRecentTrackList(newList : ArrayList<Track>) {
        val gson = Gson()
        val jsonString = gson.toJson(newList)
        preferences.edit().putString(Constants.RECENT_TRACKS_KEY, jsonString).apply()
    }

    override fun decodeRecentTrackList() : ArrayList<Track> {
        val jsonString = preferences.getString(Constants.RECENT_TRACKS_KEY, "empty")

        if (jsonString == "empty") {
            return arrayListOf<Track>()
        } else {
            val type = object : TypeToken<java.util.ArrayList<Track>>() {}.type
            val tracks = Gson().fromJson<java.util.ArrayList<Track>>(jsonString, type)
            return tracks
        }
    }
}