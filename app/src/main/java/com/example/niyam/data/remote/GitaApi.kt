package com.example.niyam.data.remote

import retrofit2.http.GET

interface GitaApi {
    @GET("chapters.json")
    suspend fun getAllChapters(): List<Chapter>

    @GET("verse.json")
    suspend fun getAllVerses(): List<Verse>

    @GET("translation.json")
    suspend fun getAllTranslations(): List<Translation>
}
