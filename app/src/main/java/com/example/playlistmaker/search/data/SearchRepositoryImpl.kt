package com.example.playlistmaker.search.data

import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.search.data.datamodels.Track
import com.example.playlistmaker.search.data.datamodels.SearchServerResonse
import com.example.playlistmaker.search.data.network.RetrofitApiClient
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.ui.SearchState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.util.Locale


class SearchRepositoryImpl(
    val preferenceManger: PreferencesManager,
    var apiClient: RetrofitApiClient,
    private val gson: Gson
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

        encodeRecentTrackList()
    }

    override fun searchITunesFlow(query: String): Flow<SearchState> = flow {

        val entity = "song"

        clearTrackList()
        try {
            emit(SearchState.LOADING)
            val response = apiClient.apiService.searchQuery(query, entity)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val parsedResponse = gson.fromJson(body, SearchServerResonse::class.java)
                    val searchResults = parsedResponse?.results
                    if (searchResults != null) {
                        if (parsedResponse.resultCount == 0) {
                            emit(SearchState.NO_RESULTS)
                        } else {
                            for (result in searchResults) {
                                addTrackToResults(
                                    Track(
                                        result.trackName,
                                        result.artistName,
                                        SimpleDateFormat("mm:ss", Locale.getDefault()).format(result.trackTimeMillis),
                                        result.artworkUrl100,
                                        result.trackId,
                                        result.collectionName,
                                        if (result.releaseDate != null && result.releaseDate.length >= 4) {
                                            result.releaseDate.substring(0, 4) //Если это магическое
                                        } else { // число, то я немного без понятия как его назвать
                                            "" //numberOfDigitsForMeasuringYearsEverSince1000ADAnd-
                                        },  //-ForTheNext~8000Years : Int?)
                                        result.primaryGenreName,
                                        result.country,
                                        result.previewUrl
                                    )
                                )
                            }
                            emit(SearchState.SHOW_RESULTS)
                        }
                    } else {
                        emit(SearchState.NO_RESULTS)
                    }
                } else {
                    emit(SearchState.NO_RESULTS)
                }
            } else {
                emit(SearchState.NETWORK_ERROR)
            }
        } catch (e: IOException) {
            emit(SearchState.NETWORK_ERROR)
        } catch (e: HttpException) {
            emit(SearchState.NETWORK_ERROR)
        }
    }


    override fun encodeRecentTrackList() {
        preferenceManger.encodeRecentTrackList(recentTrackList)
    }

    override fun decodeRecentTrackList() {
        recentTrackList = preferenceManger.decodeRecentTrackList()
    }

}