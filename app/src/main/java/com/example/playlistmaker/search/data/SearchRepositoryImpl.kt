package com.example.playlistmaker.search.data


import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.data.datamodels.searchServerResonse
import com.example.playlistmaker.search.data.network.RetrofitApiClient
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.ui.SearchState
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class SearchRepositoryImpl(
    val preferenceManger: PreferencesManager,
    var apiClient: RetrofitApiClient
) : SearchRepository {

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

        preferenceManger.encodeRecentTrackList(recentTrackList)
    }

    override fun searchITunes(query: String, callback: SearchCallback) {
        val gson = Gson()
        val entity = "song"
        val call = apiClient.apiService.searchQuery(query, entity)

        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(
                call: Call<JsonObject>,
                response: Response<JsonObject>
            ) {
                if (response.isSuccessful) {
                    clearTrackList()
                    if (response.code() == 200) {

                        if (response.body() != null) {
                            val parsedResponse = gson.fromJson(
                                response.body(),
                                searchServerResonse::class.java
                            )
                            val searchResults = parsedResponse?.results
                            if (searchResults != null) {

                                if (parsedResponse.resultCount == 0) {
                                    callback.onSearchCompleted(SearchState.NO_RESULTS)

                                } else {
                                    for (result in searchResults) {
                                        addTrackToResults(
                                            Track(
                                                result.trackName,
                                                result.artistName,
                                                SimpleDateFormat(
                                                    "mm:ss",
                                                    Locale.getDefault()
                                                ).format(
                                                    result.trackTimeMillis
                                                ),
                                                result.artworkUrl100,
                                                result.trackId,
                                                result.collectionName,
                                                if (result.releaseDate != null && result.releaseDate.length >= 4) {
                                                    result.releaseDate.substring(0, 4)
                                                } else {
                                                    ""
                                                },
                                                result.primaryGenreName,
                                                result.country,
                                                result.previewUrl
                                            )
                                        )

                                    }
                                    callback.onSearchCompleted(SearchState.SHOW_RESULTS)
                                }
                            } else {
                                callback.onSearchCompleted(SearchState.NO_RESULTS)
                            }
                        } else {
                            callback.onSearchCompleted(SearchState.NO_RESULTS)
                        }

                    } else {
                        callback.onSearchCompleted(SearchState.NETWORK_ERROR)
                    }
                } else {
                    callback.onSearchCompleted(SearchState.NETWORK_ERROR)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback.onSearchCompleted(SearchState.NETWORK_ERROR)

            }
        })

        callback.onSearchCompleted(SearchState.LOADING)
    }

    override fun encodeRecentTrackList() {
        preferenceManger.encodeRecentTrackList(recentTrackList)
    }

    override fun decodeRecentTrackList() {
        recentTrackList = preferenceManger.decodeRecentTrackList()
    }

}