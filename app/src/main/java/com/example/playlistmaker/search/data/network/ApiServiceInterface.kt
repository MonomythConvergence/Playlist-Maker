package com.example.playlistmaker.search.data.network

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceInterface {
        @GET("/search")
        suspend fun searchQuery(@Query("term") term: String, @Query("entity") entity: String): Response<JsonObject>
    }