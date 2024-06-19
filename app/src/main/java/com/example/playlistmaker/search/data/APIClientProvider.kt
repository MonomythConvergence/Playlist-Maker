package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.network.RetrofitApiClient

object APICLientProvider {

    private val retrofitApiClient: RetrofitApiClient by lazy { RetrofitApiClient() }

    fun provideApiClient(): RetrofitApiClient {
        return retrofitApiClient
    }
}