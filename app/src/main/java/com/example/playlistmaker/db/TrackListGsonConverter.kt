package com.example.playlistmaker.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TrackListGsonConverter {
    private val gson = Gson()

    @TypeConverter
    fun toListOfTracks(value: String): List<Long> {
        val type = object : TypeToken<List<Long>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toGson(list: List<Long>): String {
        return gson.toJson(list)
    }
}