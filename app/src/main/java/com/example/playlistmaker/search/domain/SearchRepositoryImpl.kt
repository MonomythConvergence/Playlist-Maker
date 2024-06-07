package com.example.playlistmaker.search.domain

import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.data.datamodels.recentTracksList
import com.example.playlistmaker.search.data.datamodels.trackList
import com.example.playlistmaker.search.data.network.RetrofitApiClient


class SearchRepositoryImpl() : SearchRepository {

    override fun isRecentListEmpty(): Boolean {
        return recentTracksList.isEmpty()
    }

    override fun clearRecentList() {
        recentTracksList.clear()
    }

    override fun clearTrackList() {
        trackList.clear()
    }

    override fun provideTrackList(): ArrayList<Track> {
        return trackList
    }

    override fun provideRecentTrackList(): ArrayList<Track> {
        return recentTracksList
    }

}