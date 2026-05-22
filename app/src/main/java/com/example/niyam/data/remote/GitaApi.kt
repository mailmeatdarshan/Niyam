package com.example.niyam.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface GitaApi {
    @GET("chapters")
    suspend fun getAllChapters(): List<Chapter>

    @GET("chapter/{chapterNumber}")
    suspend fun getChapter(@Path("chapterNumber") chapterNumber: Int): Chapter

    @GET("slok/{chapterNumber}/{verseNumber}")
    suspend fun getVerse(
        @Path("chapterNumber") chapterNumber: Int,
        @Path("verseNumber") verseNumber: Int
    ): Verse
}
