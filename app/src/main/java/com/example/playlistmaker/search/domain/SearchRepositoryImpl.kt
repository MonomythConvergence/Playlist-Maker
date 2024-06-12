package com.example.playlistmaker.search.domain

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.Constants
import com.example.playlistmaker.search.data.datamodels.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SearchRepositoryImpl(private val preferences: SharedPreferences) : SearchRepository {

    var trackList = ArrayList<Track>()
    var recentTrackList = ArrayList<Track>()

    init {
        decodeRecentTrackList()
    }

    override fun isRecentListEmpty(): Boolean {
        return recentTrackList.isEmpty()
    }

    override fun clearRecentList() {
        recentTrackList.clear()
    }

    override fun clearTrackList() {
        trackList.clear()
    }

    override fun provideTrackList(): ArrayList<Track> {
        return trackList
    }

    override fun provideRecentTrackList(): ArrayList<Track> {
        return recentTrackList
    }

    override fun addTrackToResults(newTrack: Track) {
        trackList.add(newTrack)
        encodeRecentTrackList()
    }

    override fun addTrackToRecent(newTrack: Track) {

        val existingTrack = recentTrackList.find { it.trackId == newTrack.trackId }

        if (existingTrack == null) {
            if (recentTrackList.size < 10) {
                recentTrackList.add(0, newTrack)
            } else {
                while (recentTrackList.size > 9) {
                    recentTrackList.removeAt(recentTrackList.size - 1)
                }
                recentTrackList.add(0, newTrack)
            }
        }
        if (existingTrack != null) {
            recentTrackList.remove(existingTrack)
            recentTrackList.add(0, newTrack)
        }

        encodeRecentTrackList()
        Log.d("MyTag", "Recent list size is ${recentTrackList.size}")
    }

    override fun encodeRecentTrackList() {

        val gson = Gson()
        val jsonString = gson.toJson(recentTrackList)
        preferences.edit().putString(Constants.RECENT_TRACKS_KEY, jsonString).apply()
    }

    override fun decodeRecentTrackList() {

        val jsonString = preferences.getString(Constants.RECENT_TRACKS_KEY, "empty")

        if (jsonString == "empty") {
            recentTrackList.clear()
        } else {
            val type = object : TypeToken<java.util.ArrayList<Track>>() {}.type
            val tracks = Gson().fromJson<java.util.ArrayList<Track>>(jsonString, type)
            recentTrackList = tracks
        }

    }


}